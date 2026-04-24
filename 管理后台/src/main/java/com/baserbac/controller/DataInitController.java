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
        return executeSqlFile("db/crm_lead_complete_v2.sql", "线索管理模块");
    }

    @Operation(summary = "初始化智能分配与回收模块数据")
    @PostMapping("/crm-assign-recycle")
    public R<Map<String, Object>> initCrmAssignRecycleData() {
        return executeSqlFile("db/crm_lead_assign_recycle_schema.sql", "智能分配与回收模块-表结构");
    }

    @Operation(summary = "初始化公海池测试数据")
    @PostMapping("/crm-public-pool-test-data")
    public R<Map<String, Object>> initCrmPublicPoolTestData() {
        return executeSqlFile("db/crm_lead_assign_recycle_test_data.sql", "公海池测试数据");
    }

    @Operation(summary = "初始化智能分配规则完整测试数据(30条以上)")
    @PostMapping("/crm-assign-full-test-data")
    public R<Map<String, Object>> initCrmAssignFullTestData() {
        return executeSqlFile("db/crm_lead_assign_recycle_test_data_v2.sql", "完整测试数据");
    }

    @Operation(summary = "初始化客户360°全景档案表结构")
    @PostMapping("/crm-customer-schema")
    public R<Map<String, Object>> initCrmCustomerSchema() {
        return executeSqlFile("db/crm_customer_schema.sql", "客户360°全景档案-表结构");
    }

    @Operation(summary = "初始化客户360°全景档案菜单配置")
    @PostMapping("/crm-customer-menu")
    public R<Map<String, Object>> initCrmCustomerMenu() {
        return executeSqlFile("db/crm_customer_menu.sql", "客户360°全景档案-菜单配置");
    }

    @Operation(summary = "初始化客户360°全景档案测试数据(35条以上)")
    @PostMapping("/crm-customer-test-data")
    public R<Map<String, Object>> initCrmCustomerTestData() {
        return executeSqlFile("db/crm_customer_test_data.sql", "客户360°全景档案-测试数据");
    }

    @Operation(summary = "验证客户360°全景档案数据")
    @GetMapping("/verify-customer-data")
    public R<Map<String, Object>> verifyCustomerData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> customerCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_customer WHERE is_deleted = 0"
            );
            Map<String, Object> contactCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_customer_contact WHERE is_deleted = 0"
            );
            Map<String, Object> followCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_customer_follow WHERE is_deleted = 0"
            );
            Map<String, Object> levelCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_customer_level WHERE is_enabled = 1 AND is_deleted = 0"
            );
            Map<String, Object> tagCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_customer_tag WHERE is_enabled = 1 AND is_deleted = 0"
            );
            
            result.put("customerCount", customerCount.get("count"));
            result.put("contactCount", contactCount.get("count"));
            result.put("followCount", followCount.get("count"));
            result.put("levelCount", levelCount.get("count"));
            result.put("tagCount", tagCount.get("count"));
            
            List<Map<String, Object>> customers = jdbcTemplate.queryForList(
                "SELECT id, customer_no, customer_name, short_name, level_code, status, create_time " +
                "FROM crm_customer WHERE is_deleted = 0 ORDER BY create_time DESC LIMIT 40"
            );
            result.put("customers", customers);
            
            List<Map<String, Object>> levels = jdbcTemplate.queryForList(
                "SELECT id, level_code, level_name, sort_order, description FROM crm_customer_level WHERE is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order"
            );
            result.put("levels", levels);
            
            List<Map<String, Object>> tags = jdbcTemplate.queryForList(
                "SELECT id, tag_name, tag_color, tag_category FROM crm_customer_tag WHERE is_enabled = 1 AND is_deleted = 0 ORDER BY sort_order"
            );
            result.put("tags", tags);
            
            return R.success(result);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return R.error(500, "验证失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证智能分配与回收模块数据")
    @GetMapping("/verify-assign-recycle")
    public R<Map<String, Object>> verifyAssignRecycleData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> ruleCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_assign_rule"
            );
            Map<String, Object> recordCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_assign_record"
            );
            Map<String, Object> publicCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_lead WHERE is_public = 1 AND is_deleted = 0"
            );
            Map<String, Object> totalLeadCount = jdbcTemplate.queryForMap(
                "SELECT COUNT(*) AS count FROM crm_lead WHERE is_deleted = 0"
            );
            
            result.put("assignRuleCount", ruleCount.get("count"));
            result.put("assignRecordCount", recordCount.get("count"));
            result.put("publicPoolCount", publicCount.get("count"));
            result.put("totalLeadCount", totalLeadCount.get("count"));
            
            List<Map<String, Object>> rules = jdbcTemplate.queryForList(
                "SELECT id, rule_name, rule_type, province, industry, assign_user_name, priority, is_enabled " +
                "FROM crm_assign_rule ORDER BY priority LIMIT 40"
            );
            result.put("rules", rules);
            
            List<Map<String, Object>> records = jdbcTemplate.queryForList(
                "SELECT id, lead_no, from_user_name, to_user_name, assign_type, rule_name, create_time " +
                "FROM crm_assign_record ORDER BY create_time DESC LIMIT 40"
            );
            result.put("records", records);
            
            return R.success(result);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return R.error(500, "验证失败: " + e.getMessage());
        }
    }

    private R<Map<String, Object>> executeSqlFile(String resourcePath, String moduleName) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            
            String sqlContent;
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                sqlContent = FileCopyUtils.copyToString(reader);
            }

            log.info("【{}】SQL文件编码检查：文件长度={}, 包含中文={}", 
                moduleName,
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
