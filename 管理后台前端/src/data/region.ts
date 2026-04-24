export interface RegionItem {
  value: string
  label: string
  children?: RegionItem[]
}

export const regionData: RegionItem[] = [
  {
    value: '北京市',
    label: '北京市',
    children: [
      {
        value: '北京市',
        label: '北京市',
        children: [
          { value: '东城区', label: '东城区' },
          { value: '西城区', label: '西城区' },
          { value: '朝阳区', label: '朝阳区' },
          { value: '丰台区', label: '丰台区' },
          { value: '海淀区', label: '海淀区' },
          { value: '通州区', label: '通州区' },
          { value: '大兴区', label: '大兴区' },
          { value: '顺义区', label: '顺义区' },
          { value: '昌平区', label: '昌平区' },
          { value: '房山区', label: '房山区' },
          { value: '石景山区', label: '石景山区' }
        ]
      }
    ]
  },
  {
    value: '上海市',
    label: '上海市',
    children: [
      {
        value: '上海市',
        label: '上海市',
        children: [
          { value: '黄浦区', label: '黄浦区' },
          { value: '徐汇区', label: '徐汇区' },
          { value: '长宁区', label: '长宁区' },
          { value: '静安区', label: '静安区' },
          { value: '普陀区', label: '普陀区' },
          { value: '虹口区', label: '虹口区' },
          { value: '杨浦区', label: '杨浦区' },
          { value: '浦东新区', label: '浦东新区' },
          { value: '闵行区', label: '闵行区' },
          { value: '宝山区', label: '宝山区' },
          { value: '嘉定区', label: '嘉定区' },
          { value: '松江区', label: '松江区' },
          { value: '青浦区', label: '青浦区' }
        ]
      }
    ]
  },
  {
    value: '广东省',
    label: '广东省',
    children: [
      { value: '广州市', label: '广州市', children: [
        { value: '越秀区', label: '越秀区' },
        { value: '荔湾区', label: '荔湾区' },
        { value: '海珠区', label: '海珠区' },
        { value: '天河区', label: '天河区' },
        { value: '白云区', label: '白云区' },
        { value: '黄埔区', label: '黄埔区' },
        { value: '番禺区', label: '番禺区' },
        { value: '花都区', label: '花都区' },
        { value: '南沙区', label: '南沙区' },
        { value: '增城区', label: '增城区' }
      ]},
      { value: '深圳市', label: '深圳市', children: [
        { value: '福田区', label: '福田区' },
        { value: '罗湖区', label: '罗湖区' },
        { value: '南山区', label: '南山区' },
        { value: '宝安区', label: '宝安区' },
        { value: '龙岗区', label: '龙岗区' },
        { value: '龙华区', label: '龙华区' },
        { value: '坪山区', label: '坪山区' },
        { value: '光明区', label: '光明区' },
        { value: '盐田区', label: '盐田区' }
      ]},
      { value: '东莞市', label: '东莞市', children: [
        { value: '东城街道', label: '东城街道' },
        { value: '南城街道', label: '南城街道' },
        { value: '万江街道', label: '万江街道' },
        { value: '莞城街道', label: '莞城街道' },
        { value: '长安镇', label: '长安镇' },
        { value: '虎门镇', label: '虎门镇' },
        { value: '厚街镇', label: '厚街镇' },
        { value: '塘厦镇', label: '塘厦镇' },
        { value: '常平镇', label: '常平镇' },
        { value: '大朗镇', label: '大朗镇' }
      ]},
      { value: '佛山市', label: '佛山市', children: [
        { value: '禅城区', label: '禅城区' },
        { value: '南海区', label: '南海区' },
        { value: '顺德区', label: '顺德区' },
        { value: '三水区', label: '三水区' },
        { value: '高明区', label: '高明区' }
      ]},
      { value: '惠州市', label: '惠州市', children: [
        { value: '惠城区', label: '惠城区' },
        { value: '惠阳区', label: '惠阳区' },
        { value: '博罗县', label: '博罗县' },
        { value: '惠东县', label: '惠东县' },
        { value: '龙门县', label: '龙门县' }
      ]},
      { value: '珠海市', label: '珠海市', children: [
        { value: '香洲区', label: '香洲区' },
        { value: '斗门区', label: '斗门区' },
        { value: '金湾区', label: '金湾区' }
      ]},
      { value: '中山市', label: '中山市', children: [
        { value: '石岐街道', label: '石岐街道' },
        { value: '东区街道', label: '东区街道' },
        { value: '火炬开发区街道', label: '火炬开发区街道' },
        { value: '小榄镇', label: '小榄镇' },
        { value: '古镇镇', label: '古镇镇' }
      ]},
      { value: '江门市', label: '江门市', children: [
        { value: '蓬江区', label: '蓬江区' },
        { value: '江海区', label: '江海区' },
        { value: '新会区', label: '新会区' },
        { value: '台山市', label: '台山市' },
        { value: '开平市', label: '开平市' },
        { value: '鹤山市', label: '鹤山市' },
        { value: '恩平市', label: '恩平市' }
      ]},
      { value: '肇庆市', label: '肇庆市', children: [
        { value: '端州区', label: '端州区' },
        { value: '鼎湖区', label: '鼎湖区' },
        { value: '高要区', label: '高要区' },
        { value: '四会市', label: '四会市' },
        { value: '广宁县', label: '广宁县' },
        { value: '怀集县', label: '怀集县' },
        { value: '封开县', label: '封开县' },
        { value: '德庆县', label: '德庆县' }
      ]}
    ]
  },
  {
    value: '浙江省',
    label: '浙江省',
    children: [
      { value: '杭州市', label: '杭州市', children: [
        { value: '上城区', label: '上城区' },
        { value: '拱墅区', label: '拱墅区' },
        { value: '西湖区', label: '西湖区' },
        { value: '滨江区', label: '滨江区' },
        { value: '萧山区', label: '萧山区' },
        { value: '余杭区', label: '余杭区' },
        { value: '富阳区', label: '富阳区' },
        { value: '临安区', label: '临安区' },
        { value: '临平区', label: '临平区' },
        { value: '钱塘区', label: '钱塘区' }
      ]},
      { value: '宁波市', label: '宁波市', children: [
        { value: '海曙区', label: '海曙区' },
        { value: '江北区', label: '江北区' },
        { value: '北仑区', label: '北仑区' },
        { value: '镇海区', label: '镇海区' },
        { value: '鄞州区', label: '鄞州区' },
        { value: '奉化区', label: '奉化区' },
        { value: '余姚市', label: '余姚市' },
        { value: '慈溪市', label: '慈溪市' },
        { value: '象山县', label: '象山县' },
        { value: '宁海县', label: '宁海县' }
      ]},
      { value: '温州市', label: '温州市', children: [
        { value: '鹿城区', label: '鹿城区' },
        { value: '龙湾区', label: '龙湾区' },
        { value: '瓯海区', label: '瓯海区' },
        { value: '洞头区', label: '洞头区' },
        { value: '永嘉县', label: '永嘉县' },
        { value: '平阳县', label: '平阳县' },
        { value: '苍南县', label: '苍南县' },
        { value: '文成县', label: '文成县' },
        { value: '泰顺县', label: '泰顺县' },
        { value: '瑞安市', label: '瑞安市' },
        { value: '乐清市', label: '乐清市' }
      ]},
      { value: '嘉兴市', label: '嘉兴市', children: [
        { value: '南湖区', label: '南湖区' },
        { value: '秀洲区', label: '秀洲区' },
        { value: '嘉善县', label: '嘉善县' },
        { value: '海盐县', label: '海盐县' },
        { value: '海宁市', label: '海宁市' },
        { value: '平湖市', label: '平湖市' },
        { value: '桐乡市', label: '桐乡市' }
      ]},
      { value: '湖州市', label: '湖州市', children: [
        { value: '吴兴区', label: '吴兴区' },
        { value: '南浔区', label: '南浔区' },
        { value: '德清县', label: '德清县' },
        { value: '长兴县', label: '长兴县' },
        { value: '安吉县', label: '安吉县' }
      ]},
      { value: '绍兴市', label: '绍兴市', children: [
        { value: '越城区', label: '越城区' },
        { value: '柯桥区', label: '柯桥区' },
        { value: '上虞区', label: '上虞区' },
        { value: '新昌县', label: '新昌县' },
        { value: '诸暨市', label: '诸暨市' },
        { value: '嵊州市', label: '嵊州市' }
      ]},
      { value: '金华市', label: '金华市', children: [
        { value: '婺城区', label: '婺城区' },
        { value: '金东区', label: '金东区' },
        { value: '武义县', label: '武义县' },
        { value: '浦江县', label: '浦江县' },
        { value: '磐安县', label: '磐安县' },
        { value: '兰溪市', label: '兰溪市' },
        { value: '义乌市', label: '义乌市' },
        { value: '东阳市', label: '东阳市' },
        { value: '永康市', label: '永康市' }
      ]},
      { value: '台州市', label: '台州市', children: [
        { value: '椒江区', label: '椒江区' },
        { value: '黄岩区', label: '黄岩区' },
        { value: '路桥区', label: '路桥区' },
        { value: '三门县', label: '三门县' },
        { value: '天台县', label: '天台县' },
        { value: '仙居县', label: '仙居县' },
        { value: '温岭市', label: '温岭市' },
        { value: '临海市', label: '临海市' },
        { value: '玉环市', label: '玉环市' }
      ]}
    ]
  },
  {
    value: '江苏省',
    label: '江苏省',
    children: [
      { value: '南京市', label: '南京市', children: [
        { value: '玄武区', label: '玄武区' },
        { value: '秦淮区', label: '秦淮区' },
        { value: '建邺区', label: '建邺区' },
        { value: '鼓楼区', label: '鼓楼区' },
        { value: '浦口区', label: '浦口区' },
        { value: '栖霞区', label: '栖霞区' },
        { value: '雨花台区', label: '雨花台区' },
        { value: '江宁区', label: '江宁区' },
        { value: '六合区', label: '六合区' },
        { value: '溧水区', label: '溧水区' },
        { value: '高淳区', label: '高淳区' }
      ]},
      { value: '苏州市', label: '苏州市', children: [
        { value: '虎丘区', label: '虎丘区' },
        { value: '吴中区', label: '吴中区' },
        { value: '相城区', label: '相城区' },
        { value: '姑苏区', label: '姑苏区' },
        { value: '吴江区', label: '吴江区' },
        { value: '常熟市', label: '常熟市' },
        { value: '张家港市', label: '张家港市' },
        { value: '昆山市', label: '昆山市' },
        { value: '太仓市', label: '太仓市' }
      ]},
      { value: '无锡市', label: '无锡市', children: [
        { value: '锡山区', label: '锡山区' },
        { value: '惠山区', label: '惠山区' },
        { value: '滨湖区', label: '滨湖区' },
        { value: '梁溪区', label: '梁溪区' },
        { value: '新吴区', label: '新吴区' },
        { value: '江阴市', label: '江阴市' },
        { value: '宜兴市', label: '宜兴市' }
      ]},
      { value: '常州市', label: '常州市', children: [
        { value: '天宁区', label: '天宁区' },
        { value: '钟楼区', label: '钟楼区' },
        { value: '新北区', label: '新北区' },
        { value: '武进区', label: '武进区' },
        { value: '金坛区', label: '金坛区' },
        { value: '溧阳市', label: '溧阳市' }
      ]},
      { value: '南通市', label: '南通市', children: [
        { value: '崇川区', label: '崇川区' },
        { value: '港闸区', label: '港闸区' },
        { value: '通州区', label: '通州区' },
        { value: '海安市', label: '海安市' },
        { value: '如东县', label: '如东县' },
        { value: '启东市', label: '启东市' },
        { value: '如皋市', label: '如皋市' },
        { value: '海门市', label: '海门市' }
      ]},
      { value: '徐州市', label: '徐州市', children: [
        { value: '鼓楼区', label: '鼓楼区' },
        { value: '云龙区', label: '云龙区' },
        { value: '贾汪区', label: '贾汪区' },
        { value: '泉山区', label: '泉山区' },
        { value: '铜山区', label: '铜山区' },
        { value: '丰县', label: '丰县' },
        { value: '沛县', label: '沛县' },
        { value: '睢宁县', label: '睢宁县' },
        { value: '新沂市', label: '新沂市' },
        { value: '邳州市', label: '邳州市' }
      ]}
    ]
  },
  {
    value: '四川省',
    label: '四川省',
    children: [
      { value: '成都市', label: '成都市', children: [
        { value: '锦江区', label: '锦江区' },
        { value: '青羊区', label: '青羊区' },
        { value: '金牛区', label: '金牛区' },
        { value: '武侯区', label: '武侯区' },
        { value: '成华区', label: '成华区' },
        { value: '龙泉驿区', label: '龙泉驿区' },
        { value: '青白江区', label: '青白江区' },
        { value: '新都区', label: '新都区' },
        { value: '温江区', label: '温江区' },
        { value: '双流区', label: '双流区' },
        { value: '郫都区', label: '郫都区' },
        { value: '新津区', label: '新津区' }
      ]},
      { value: '绵阳市', label: '绵阳市', children: [
        { value: '涪城区', label: '涪城区' },
        { value: '游仙区', label: '游仙区' },
        { value: '安州区', label: '安州区' },
        { value: '三台县', label: '三台县' },
        { value: '盐亭县', label: '盐亭县' },
        { value: '梓潼县', label: '梓潼县' },
        { value: '北川羌族自治县', label: '北川羌族自治县' },
        { value: '平武县', label: '平武县' },
        { value: '江油市', label: '江油市' }
      ]},
      { value: '德阳市', label: '德阳市', children: [
        { value: '旌阳区', label: '旌阳区' },
        { value: '罗江区', label: '罗江区' },
        { value: '中江县', label: '中江县' },
        { value: '广汉市', label: '广汉市' },
        { value: '什邡市', label: '什邡市' },
        { value: '绵竹市', label: '绵竹市' }
      ]},
      { value: '攀枝花市', label: '攀枝花市', children: [
        { value: '东区', label: '东区' },
        { value: '西区', label: '西区' },
        { value: '仁和区', label: '仁和区' },
        { value: '米易县', label: '米易县' },
        { value: '盐边县', label: '盐边县' }
      ]}
    ]
  },
  {
    value: '湖北省',
    label: '湖北省',
    children: [
      { value: '武汉市', label: '武汉市', children: [
        { value: '江岸区', label: '江岸区' },
        { value: '江汉区', label: '江汉区' },
        { value: '硚口区', label: '硚口区' },
        { value: '汉阳区', label: '汉阳区' },
        { value: '武昌区', label: '武昌区' },
        { value: '青山区', label: '青山区' },
        { value: '洪山区', label: '洪山区' },
        { value: '东西湖区', label: '东西湖区' },
        { value: '汉南区', label: '汉南区' },
        { value: '蔡甸区', label: '蔡甸区' },
        { value: '江夏区', label: '江夏区' },
        { value: '黄陂区', label: '黄陂区' },
        { value: '新洲区', label: '新洲区' }
      ]},
      { value: '宜昌市', label: '宜昌市', children: [
        { value: '西陵区', label: '西陵区' },
        { value: '伍家岗区', label: '伍家岗区' },
        { value: '点军区', label: '点军区' },
        { value: '猇亭区', label: '猇亭区' },
        { value: '夷陵区', label: '夷陵区' },
        { value: '远安县', label: '远安县' },
        { value: '兴山县', label: '兴山县' },
        { value: '秭归县', label: '秭归县' },
        { value: '长阳土家族自治县', label: '长阳土家族自治县' },
        { value: '五峰土家族自治县', label: '五峰土家族自治县' },
        { value: '宜都市', label: '宜都市' },
        { value: '当阳市', label: '当阳市' },
        { value: '枝江市', label: '枝江市' }
      ]},
      { value: '黄石市', label: '黄石市', children: [
        { value: '黄石港区', label: '黄石港区' },
        { value: '西塞山区', label: '西塞山区' },
        { value: '下陆区', label: '下陆区' },
        { value: '铁山区', label: '铁山区' },
        { value: '阳新县', label: '阳新县' },
        { value: '大冶市', label: '大冶市' }
      ]}
    ]
  },
  {
    value: '重庆市',
    label: '重庆市',
    children: [
      {
        value: '重庆市',
        label: '重庆市',
        children: [
          { value: '万州区', label: '万州区' },
          { value: '涪陵区', label: '涪陵区' },
          { value: '渝中区', label: '渝中区' },
          { value: '大渡口区', label: '大渡口区' },
          { value: '江北区', label: '江北区' },
          { value: '沙坪坝区', label: '沙坪坝区' },
          { value: '九龙坡区', label: '九龙坡区' },
          { value: '南岸区', label: '南岸区' },
          { value: '北碚区', label: '北碚区' },
          { value: '綦江区', label: '綦江区' },
          { value: '大足区', label: '大足区' },
          { value: '渝北区', label: '渝北区' },
          { value: '巴南区', label: '巴南区' },
          { value: '黔江区', label: '黔江区' },
          { value: '长寿区', label: '长寿区' },
          { value: '合川区', label: '合川区' },
          { value: '永川区', label: '永川区' },
          { value: '南川区', label: '南川区' },
          { value: '璧山区', label: '璧山区' },
          { value: '铜梁区', label: '铜梁区' },
          { value: '潼南区', label: '潼南区' },
          { value: '荣昌区', label: '荣昌区' }
        ]
      }
    ]
  },
  {
    value: '陕西省',
    label: '陕西省',
    children: [
      { value: '西安市', label: '西安市', children: [
        { value: '新城区', label: '新城区' },
        { value: '碑林区', label: '碑林区' },
        { value: '莲湖区', label: '莲湖区' },
        { value: '灞桥区', label: '灞桥区' },
        { value: '未央区', label: '未央区' },
        { value: '雁塔区', label: '雁塔区' },
        { value: '阎良区', label: '阎良区' },
        { value: '临潼区', label: '临潼区' },
        { value: '长安区', label: '长安区' },
        { value: '高陵区', label: '高陵区' },
        { value: '鄠邑区', label: '鄠邑区' },
        { value: '蓝田县', label: '蓝田县' },
        { value: '周至县', label: '周至县' }
      ]},
      { value: '宝鸡市', label: '宝鸡市', children: [
        { value: '渭滨区', label: '渭滨区' },
        { value: '金台区', label: '金台区' },
        { value: '陈仓区', label: '陈仓区' },
        { value: '凤翔区', label: '凤翔区' },
        { value: '岐山县', label: '岐山县' },
        { value: '扶风县', label: '扶风县' },
        { value: '眉县', label: '眉县' },
        { value: '陇县', label: '陇县' },
        { value: '千阳县', label: '千阳县' },
        { value: '麟游县', label: '麟游县' },
        { value: '凤县', label: '凤县' },
        { value: '太白县', label: '太白县' }
      ]},
      { value: '咸阳市', label: '咸阳市', children: [
        { value: '秦都区', label: '秦都区' },
        { value: '杨陵区', label: '杨陵区' },
        { value: '渭城区', label: '渭城区' },
        { value: '三原县', label: '三原县' },
        { value: '泾阳县', label: '泾阳县' },
        { value: '乾县', label: '乾县' },
        { value: '礼泉县', label: '礼泉县' },
        { value: '永寿县', label: '永寿县' },
        { value: '长武县', label: '长武县' },
        { value: '旬邑县', label: '旬邑县' },
        { value: '淳化县', label: '淳化县' },
        { value: '武功县', label: '武功县' },
        { value: '兴平市', label: '兴平市' },
        { value: '彬州市', label: '彬州市' }
      ]}
    ]
  },
  {
    value: '河南省',
    label: '河南省',
    children: [
      { value: '郑州市', label: '郑州市', children: [
        { value: '中原区', label: '中原区' },
        { value: '二七区', label: '二七区' },
        { value: '管城回族区', label: '管城回族区' },
        { value: '金水区', label: '金水区' },
        { value: '上街区', label: '上街区' },
        { value: '惠济区', label: '惠济区' },
        { value: '中牟县', label: '中牟县' },
        { value: '巩义市', label: '巩义市' },
        { value: '荥阳市', label: '荥阳市' },
        { value: '新密市', label: '新密市' },
        { value: '新郑市', label: '新郑市' },
        { value: '登封市', label: '登封市' }
      ]},
      { value: '洛阳市', label: '洛阳市', children: [
        { value: '老城区', label: '老城区' },
        { value: '西工区', label: '西工区' },
        { value: '瀍河回族区', label: '瀍河回族区' },
        { value: '涧西区', label: '涧西区' },
        { value: '吉利区', label: '吉利区' },
        { value: '洛龙区', label: '洛龙区' },
        { value: '孟津区', label: '孟津区' },
        { value: '新安县', label: '新安县' },
        { value: '栾川县', label: '栾川县' },
        { value: '嵩县', label: '嵩县' },
        { value: '汝阳县', label: '汝阳县' },
        { value: '宜阳县', label: '宜阳县' },
        { value: '洛宁县', label: '洛宁县' },
        { value: '伊川县', label: '伊川县' },
        { value: '偃师区', label: '偃师区' }
      ]},
      { value: '开封市', label: '开封市', children: [
        { value: '龙亭区', label: '龙亭区' },
        { value: '顺河回族区', label: '顺河回族区' },
        { value: '鼓楼区', label: '鼓楼区' },
        { value: '禹王台区', label: '禹王台区' },
        { value: '祥符区', label: '祥符区' },
        { value: '杞县', label: '杞县' },
        { value: '通许县', label: '通许县' },
        { value: '尉氏县', label: '尉氏县' },
        { value: '兰考县', label: '兰考县' }
      ]}
    ]
  },
  {
    value: '福建省',
    label: '福建省',
    children: [
      { value: '福州市', label: '福州市', children: [
        { value: '鼓楼区', label: '鼓楼区' },
        { value: '台江区', label: '台江区' },
        { value: '仓山区', label: '仓山区' },
        { value: '马尾区', label: '马尾区' },
        { value: '晋安区', label: '晋安区' },
        { value: '长乐区', label: '长乐区' },
        { value: '闽侯县', label: '闽侯县' },
        { value: '连江县', label: '连江县' },
        { value: '罗源县', label: '罗源县' },
        { value: '闽清县', label: '闽清县' },
        { value: '永泰县', label: '永泰县' },
        { value: '平潭县', label: '平潭县' },
        { value: '福清市', label: '福清市' }
      ]},
      { value: '厦门市', label: '厦门市', children: [
        { value: '思明区', label: '思明区' },
        { value: '海沧区', label: '海沧区' },
        { value: '湖里区', label: '湖里区' },
        { value: '集美区', label: '集美区' },
        { value: '同安区', label: '同安区' },
        { value: '翔安区', label: '翔安区' }
      ]},
      { value: '莆田市', label: '莆田市', children: [
        { value: '城厢区', label: '城厢区' },
        { value: '涵江区', label: '涵江区' },
        { value: '荔城区', label: '荔城区' },
        { value: '秀屿区', label: '秀屿区' },
        { value: '仙游县', label: '仙游县' }
      ]},
      { value: '三明市', label: '三明市', children: [
        { value: '梅列区', label: '梅列区' },
        { value: '三元区', label: '三元区' },
        { value: '明溪县', label: '明溪县' },
        { value: '清流县', label: '清流县' },
        { value: '宁化县', label: '宁化县' },
        { value: '大田县', label: '大田县' },
        { value: '尤溪县', label: '尤溪县' },
        { value: '沙县', label: '沙县' },
        { value: '将乐县', label: '将乐县' },
        { value: '泰宁县', label: '泰宁县' },
        { value: '建宁县', label: '建宁县' },
        { value: '永安市', label: '永安市' }
      ]},
      { value: '泉州市', label: '泉州市', children: [
        { value: '鲤城区', label: '鲤城区' },
        { value: '丰泽区', label: '丰泽区' },
        { value: '洛江区', label: '洛江区' },
        { value: '泉港区', label: '泉港区' },
        { value: '惠安县', label: '惠安县' },
        { value: '安溪县', label: '安溪县' },
        { value: '永春县', label: '永春县' },
        { value: '德化县', label: '德化县' },
        { value: '金门县', label: '金门县' },
        { value: '石狮市', label: '石狮市' },
        { value: '晋江市', label: '晋江市' },
        { value: '南安市', label: '南安市' }
      ]}
    ]
  },
  {
    value: '山东省',
    label: '山东省',
    children: [
      { value: '济南市', label: '济南市', children: [
        { value: '历下区', label: '历下区' },
        { value: '市中区', label: '市中区' },
        { value: '槐荫区', label: '槐荫区' },
        { value: '天桥区', label: '天桥区' },
        { value: '历城区', label: '历城区' },
        { value: '长清区', label: '长清区' },
        { value: '章丘区', label: '章丘区' },
        { value: '济阳区', label: '济阳区' },
        { value: '莱芜区', label: '莱芜区' },
        { value: '钢城区', label: '钢城区' },
        { value: '平阴县', label: '平阴县' },
        { value: '商河县', label: '商河县' }
      ]},
      { value: '青岛市', label: '青岛市', children: [
        { value: '市南区', label: '市南区' },
        { value: '市北区', label: '市北区' },
        { value: '黄岛区', label: '黄岛区' },
        { value: '崂山区', label: '崂山区' },
        { value: '李沧区', label: '李沧区' },
        { value: '城阳区', label: '城阳区' },
        { value: '即墨区', label: '即墨区' },
        { value: '胶州市', label: '胶州市' },
        { value: '平度市', label: '平度市' },
        { value: '莱西市', label: '莱西市' }
      ]},
      { value: '淄博市', label: '淄博市', children: [
        { value: '淄川区', label: '淄川区' },
        { value: '张店区', label: '张店区' },
        { value: '博山区', label: '博山区' },
        { value: '临淄区', label: '临淄区' },
        { value: '周村区', label: '周村区' },
        { value: '桓台县', label: '桓台县' },
        { value: '高青县', label: '高青县' },
        { value: '沂源县', label: '沂源县' }
      ]},
      { value: '烟台市', label: '烟台市', children: [
        { value: '芝罘区', label: '芝罘区' },
        { value: '福山区', label: '福山区' },
        { value: '牟平区', label: '牟平区' },
        { value: '莱山区', label: '莱山区' },
        { value: '蓬莱区', label: '蓬莱区' },
        { value: '龙口市', label: '龙口市' },
        { value: '莱阳市', label: '莱阳市' },
        { value: '莱州市', label: '莱州市' },
        { value: '招远市', label: '招远市' },
        { value: '栖霞市', label: '栖霞市' },
        { value: '海阳市', label: '海阳市' }
      ]}
    ]
  },
  {
    value: '辽宁省',
    label: '辽宁省',
    children: [
      { value: '沈阳市', label: '沈阳市', children: [
        { value: '和平区', label: '和平区' },
        { value: '沈河区', label: '沈河区' },
        { value: '大东区', label: '大东区' },
        { value: '皇姑区', label: '皇姑区' },
        { value: '铁西区', label: '铁西区' },
        { value: '苏家屯区', label: '苏家屯区' },
        { value: '浑南区', label: '浑南区' },
        { value: '沈北新区', label: '沈北新区' },
        { value: '于洪区', label: '于洪区' },
        { value: '辽中区', label: '辽中区' },
        { value: '康平县', label: '康平县' },
        { value: '法库县', label: '法库县' },
        { value: '新民市', label: '新民市' }
      ]},
      { value: '大连市', label: '大连市', children: [
        { value: '中山区', label: '中山区' },
        { value: '西岗区', label: '西岗区' },
        { value: '沙河口区', label: '沙河口区' },
        { value: '甘井子区', label: '甘井子区' },
        { value: '旅顺口区', label: '旅顺口区' },
        { value: '金州区', label: '金州区' },
        { value: '普兰店区', label: '普兰店区' },
        { value: '长海县', label: '长海县' },
        { value: '瓦房店市', label: '瓦房店市' },
        { value: '庄河市', label: '庄河市' }
      ]},
      { value: '鞍山市', label: '鞍山市', children: [
        { value: '铁东区', label: '铁东区' },
        { value: '铁西区', label: '铁西区' },
        { value: '立山区', label: '立山区' },
        { value: '千山区', label: '千山区' },
        { value: '台安县', label: '台安县' },
        { value: '岫岩满族自治县', label: '岫岩满族自治县' },
        { value: '海城市', label: '海城市' }
      ]}
    ]
  },
  {
    value: '天津市',
    label: '天津市',
    children: [
      {
        value: '天津市',
        label: '天津市',
        children: [
          { value: '和平区', label: '和平区' },
          { value: '河东区', label: '河东区' },
          { value: '河西区', label: '河西区' },
          { value: '南开区', label: '南开区' },
          { value: '河北区', label: '河北区' },
          { value: '红桥区', label: '红桥区' },
          { value: '东丽区', label: '东丽区' },
          { value: '西青区', label: '西青区' },
          { value: '津南区', label: '津南区' },
          { value: '北辰区', label: '北辰区' },
          { value: '武清区', label: '武清区' },
          { value: '宝坻区', label: '宝坻区' },
          { value: '滨海新区', label: '滨海新区' },
          { value: '宁河区', label: '宁河区' },
          { value: '静海区', label: '静海区' },
          { value: '蓟州区', label: '蓟州区' }
        ]
      }
    ]
  }
]

export const provinceList = regionData.map(item => ({ value: item.value, label: item.label }))

export const industryList = [
  { value: 'IT互联网', label: 'IT互联网' },
  { value: '金融', label: '金融' },
  { value: '制造业', label: '制造业' },
  { value: '零售', label: '零售' },
  { value: '电商', label: '电商' },
  { value: '教育培训', label: '教育培训' },
  { value: '医疗健康', label: '医疗健康' },
  { value: '餐饮', label: '餐饮' },
  { value: '旅游服务', label: '旅游服务' },
  { value: '物流', label: '物流' },
  { value: '建筑工程', label: '建筑工程' },
  { value: '广告传媒', label: '广告传媒' },
  { value: '物业服务', label: '物业服务' },
  { value: '商务服务', label: '商务服务' },
  { value: '化工', label: '化工' },
  { value: '医药健康', label: '医药健康' },
  { value: '金融保险', label: '金融保险' },
  { value: '农业', label: '农业' },
  { value: '设计服务', label: '设计服务' },
  { value: '汽车销售', label: '汽车销售' },
  { value: '生活服务', label: '生活服务' },
  { value: '其他', label: '其他' }
]

export function getCityList(province: string): RegionItem[] {
  const provinceItem = regionData.find(item => item.value === province)
  if (provinceItem && provinceItem.children) {
    return provinceItem.children
  }
  return []
}

export function getDistrictList(province: string, city: string): RegionItem[] {
  const provinceItem = regionData.find(item => item.value === province)
  if (provinceItem && provinceItem.children) {
    const cityItem = provinceItem.children.find(item => item.value === city)
    if (cityItem && cityItem.children) {
      return cityItem.children
    }
  }
  return []
}
