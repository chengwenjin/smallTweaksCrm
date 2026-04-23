package com.baserbac.controller;

import com.baserbac.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "数据初始化")
@RestController
@RequestMapping("/api/init")
public class DataInitController {

    private static final Logger log = LoggerFactory.getLogger(DataInitController.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Operation(summary = "初始化线索管理模块数据")
    @PostMapping("/crm-lead")
    public R<Map<String, Object>> initCrmLeadData() {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        try {
            ClassPathResource resource = new ClassPathResource("db/crm_lead_complete_v2.sql");
            
            String sqlContent;
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                sqlContent = FileCopyUtils.copyToString(reader);
            }

            log.info("SQL文件编码检查：文件长度={}, 包含中文={}", 
                sqlContent.length(), 
                sqlContent.contains("客户") || sqlContent.contains("线索"));

            List<String> statements = parseSqlStatements(sqlContent);
            
            log.info("解析到 {} 条SQL语句", statements.size());

            for (int i = 0; i < statements.size(); i++) {
                String sql = statements.get(i).trim();
                if (sql.isEmpty() || sql.startsWith("--") || sql.startsWith("/*")) {
                    continue;
                }

                try {
                    if (isSelectStatement(sql)) {
                        continue;
                    }
                    
                    if (sql.toLowerCase().startsWith("set ")) {
                        try {
                            jdbcTemplate.execute(sql);
                            log.info("执行SET语句成功: {}", sql);
                        } catch (Exception e) {
                            log.warn("SET语句执行失败（忽略）: {} - {}", sql, e.getMessage());
                        }
                        continue;
                    }
                    
                    jdbcTemplate.execute(sql);
                    successCount++;
                    log.info("执行成功 ({}/{}): {}", i + 1, statements.size(), 
                        sql.length() > 100 ? sql.substring(0, 100) + "..." : sql);
                } catch (Exception e) {
                    String errorMsg = String.format("语句 %d 执行失败: %s. 错误: %s", 
                        i + 1, 
                        sql.length() > 200 ? sql.substring(0, 200) + "..." : sql,
                        e.getMessage());
                    log.error(errorMsg);
                    errors.add(errorMsg);
                }
            }

            result.put("successCount", successCount);
            result.put("errorCount", errors.size());
            result.put("errors", errors);
            result.put("message", errors.isEmpty() ? "数据初始化成功" : "部分语句执行失败");

            return R.success(result);

        } catch (Exception e) {
            log.error("数据初始化失败", e);
            result.put("error", e.getMessage());
            return R.error(500, "数据初始化失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证菜单数据")
    @GetMapping("/verify-menu")
    public R<Map<String, Object>> verifyMenuData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> menus = jdbcTemplate.queryForList(
                "SELECT id, parent_id, menu_name, menu_type, path, component, permission_key " +
                "FROM sys_menu WHERE menu_name LIKE '%客户%' OR menu_name LIKE '%线索%' OR path LIKE '/crm%' OR component LIKE 'crm/%' ORDER BY parent_id, sort_order"
            );
            
            result.put("menuCount", menus.size());
            result.put("menus", menus);
            
            return R.success(result);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return R.error(500, "验证失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证线索测试数据")
    @GetMapping("/verify-lead-data")
    public R<Map<String, Object>> verifyLeadData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> leads = jdbcTemplate.queryForList(
                "SELECT id, lead_no, lead_name, contact_name, contact_mobile, " +
                "source_name, level, status, create_time " +
                "FROM crm_lead WHERE is_deleted = 0 ORDER BY create_time DESC"
            );
            
            List<Map<String, Object>> sources = jdbcTemplate.queryForList(
                "SELECT id, source_code, source_name, source_type, is_enabled " +
                "FROM crm_lead_source ORDER BY sort_order"
            );
            
            result.put("leadCount", leads.size());
            result.put("leads", leads);
            result.put("sourceCount", sources.size());
            result.put("sources", sources);
            
            return R.success(result);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return R.error(500, "验证失败: " + e.getMessage());
        }
    }

    private List<String> parseSqlStatements(String sqlContent) {
        List<String> statements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inString = false;
        boolean inSingleLineComment = false;
        boolean inMultiLineComment = false;
        char stringChar = 0;
        
        for (int i = 0; i < sqlContent.length(); i++) {
            char c = sqlContent.charAt(i);
            char nextChar = (i < sqlContent.length() - 1) ? sqlContent.charAt(i + 1) : 0;
            
            if (inSingleLineComment) {
                if (c == '\n') {
                    inSingleLineComment = false;
                }
                continue;
            }
            
            if (inMultiLineComment) {
                if (c == '*' && nextChar == '/') {
                    inMultiLineComment = false;
                    i++;
                }
                continue;
            }
            
            if (!inString) {
                if (c == '-' && nextChar == '-') {
                    inSingleLineComment = true;
                    i++;
                    continue;
                }
                if (c == '/' && nextChar == '*') {
                    inMultiLineComment = true;
                    i++;
                    continue;
                }
            }
            
            if ((c == '\'' || c == '\"') && !inString) {
                inString = true;
                stringChar = c;
                sb.append(c);
                continue;
            }
            
            if (inString && c == stringChar) {
                if (i > 0 && sqlContent.charAt(i - 1) == '\\') {
                    sb.append(c);
                    continue;
                }
                inString = false;
                sb.append(c);
                continue;
            }
            
            if (c == ';' && !inString) {
                String stmt = sb.toString().trim();
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                }
                sb.setLength(0);
                continue;
            }
            
            if (!inString && (c == '\n' || c == '\r')) {
                sb.append(' ');
                continue;
            }
            
            sb.append(c);
        }
        
        String lastStmt = sb.toString().trim();
        if (!lastStmt.isEmpty()) {
            statements.add(lastStmt);
        }
        
        return statements;
    }

    private boolean isSelectStatement(String sql) {
        String lowerSql = sql.toLowerCase().trim();
        return lowerSql.startsWith("select ") && !lowerSql.contains("into ");
    }
}
