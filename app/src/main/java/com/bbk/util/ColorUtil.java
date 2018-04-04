package com.bbk.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

public class ColorUtil {
	public static final String LightPink = "#FFB6C1";// 255,182,193 浅粉色
	public static final String Pink = "#FFC0CB";// 255,192,203 粉红
	public static final String Crimson = "#DC143C";// 220,20,60 猩红
	public static final String LavenderBlush = "#FFF0F5";// 255,240,245 脸红的淡紫色
	public static final String PaleVioletRed = "#DB7093";// 219,112,147 苍白的紫罗兰红色
	public static final String HotPink = "#FF69B4";// 255,105,180 热情的粉红
	public static final String DeepPink = "#FF1493";// 255,20,147 深粉色
	public static final String MediumVioletRed = "#C71585";// 199,21,133
															// 适中的紫罗兰红色
	public static final String Orchid = "#DA70D6";// 218,112,214 兰花的紫色
	public static final String Thistle = "#D8BFD8";// 216,191,216 蓟
	public static final String plum = "#DDA0DD";// 221,160,221 李子
	public static final String Violet = "#EE82EE";// 238,130,238 紫罗兰
	public static final String Magenta = "#FF00FF";// 255,0,255 洋红
	public static final String Fuchsia = "#FF00FF";// 255,0,255 灯笼海棠(紫红色)
	public static final String DarkMagenta = "#8B008B";// 139,0,139 深洋红色
	public static final String Purple = "#800080";// 128,0,128 紫色
	public static final String MediumOrchid = "#BA55D3";// 186,85,211 适中的兰花紫
	public static final String DarkVoilet = "#9400D3";// 148,0,211 深紫罗兰色
	public static final String DarkOrchid = "#9932CC";// 153,50,204 深兰花紫
	public static final String Indigo = "#4B0082";// 75,0,130 靛青
	public static final String BlueViolet = "#8A2BE2";// 138,43,226 深紫罗兰的蓝色
	public static final String MediumPurple = "#9370DB";// 147,112,219 适中的紫色
	public static final String MediumSlateBlue = "#7B68EE";// 123,104,238
															// 适中的板岩暗蓝灰色
	public static final String SlateBlue = "#6A5ACD";// 106,90,205 板岩暗蓝灰色
	public static final String DarkSlateBlue = "#483D8B";// 72,61,139 深岩暗蓝灰色
	public static final String Lavender = "#E6E6FA";// 230,230,250 薰衣草花的淡紫色
	public static final String GhostWhite = "#F8F8FF";// 248,248,255 幽灵的白色
	public static final String Blue = "#0000FF";// 0,0,255 纯蓝
	public static final String MediumBlue = "#0000CD";// 0,0,205 适中的蓝色
	public static final String MidnightBlue = "#191970";// 25,25,112 午夜的蓝色
//	public static final String DarkBlue = "#00008B";// 0,0,139 深蓝色
	public static final String Navy = "#000080";// 0,0,128 海军蓝
	public static final String RoyalBlue = "#4169E1";// 65,105,225 皇军蓝
	public static final String CornflowerBlue = "#6495ED";// 100,149,237 矢车菊的蓝色
	public static final String LightSteelBlue = "#B0C4DE";// 176,196,222 淡钢蓝
	public static final String LightSlateGray = "#778899";// 119,136,153 浅石板灰
	public static final String SlateGray = "#708090";// 112,128,144 石板灰
	public static final String DoderBlue = "#1E90FF";// 30,144,255 道奇蓝
//	public static final String AliceBlue = "#F0F8FF";// 240,248,255 爱丽丝蓝
	public static final String SteelBlue = "#4682B4";// 70,130,180 钢蓝
	public static final String LightSkyBlue = "#87CEFA";// 135,206,250 淡蓝色
	public static final String SkyBlue = "#87CEEB";// 135,206,235 天蓝色
	public static final String DeepSkyBlue = "#00BFFF";// 0,191,255 深天蓝
	public static final String LightBLue = "#ADD8E6";// 173,216,230 淡蓝
	public static final String PowDerBlue = "#B0E0E6";// 176,224,230 火药蓝
	public static final String CadetBlue = "#5F9EA0";// 95,158,160 军校蓝
//	public static final String Azure = "#F0FFFF";// 240,255,255 蔚蓝色
	public static final String LightCyan = "#E1FFFF";// 225,255,255 淡青色
	public static final String PaleTurquoise = "#AFEEEE";// 175,238,238 苍白的绿宝石
	public static final String Cyan = "#00FFFF";// 0,255,255 青色
//	public static final String Aqua = "#00FFFF";// 0,255,255 水绿色
	public static final String DarkTurquoise = "#00CED1";// 0,206,209 深绿宝石
	public static final String DarkSlateGray = "#2F4F4F";// 47,79,79 深石板灰
//	public static final String DarkCyan = "#008B8B";// 0,139,139 深青色
	public static final String Teal = "#008080";// 0,128,128 水鸭色
	public static final String MediumTurquoise = "#48D1CC";// 72,209,204 适中的绿宝石
	public static final String LightSeaGreen = "#20B2AA";// 32,178,170 浅海洋绿
	public static final String Turquoise = "#40E0D0";// 64,224,208 绿宝石
	public static final String Auqamarin = "#7FFFAA";// 127,255,170 绿玉碧绿色
	public static final String MediumAquamarine = "#00FA9A";// 0,250,154 适中的碧绿色
	public static final String MediumSpringGreen = "#F5FFFA";// 245,255,250
																// 适中的春天的绿色
	public static final String MintCream = "#00FF7F";// 0,255,127 薄荷奶油
	public static final String SpringGreen = "#3CB371";// 60,179,113 春天的绿色
	public static final String SeaGreen = "#2E8B57";// 46,139,87 海洋绿
	public static final String Honeydew = "#F0FFF0";// 240,255,240 蜂蜜
	public static final String LightGreen = "#90EE90";// 144,238,144 淡绿色
	public static final String PaleGreen = "#98FB98";// 152,251,152 苍白的绿色
	public static final String DarkSeaGreen = "#8FBC8F";// 143,188,143 深海洋绿
	public static final String LimeGreen = "#32CD32";// 50,205,50 酸橙绿
	public static final String Lime = "#00FF00";// 0,255,0 酸橙色
	public static final String ForestGreen = "#228B22";// 34,139,34 森林绿
	public static final String Green = "#008000";// 0,128,0 纯绿
	public static final String DarkGreen = "#006400";// 0,100,0 深绿色
//	public static final String Chartreuse = "#7FFF00";// 127,255,0 查特酒绿
	public static final String LawnGreen = "#7CFC00";// 124,252,0 草坪绿
	public static final String GreenYellow = "#ADFF2F";// 173,255,47 绿黄色
	public static final String OliveDrab = "#556B2F";// 85,107,47 橄榄土褐色
	public static final String Beige = "#6B8E23";// 107,142,35 米色(浅褐色)
	public static final String LightGoldenrodYellow = "#FAFAD2";// 250,250,210
																// 浅秋麒麟黄
	public static final String Ivory = "#FFFFF0";// 255,255,240 象牙
	public static final String LightYellow = "#FFFFE0";// 255,255,224 浅黄色
	public static final String Yellow = "#FFFF00";// 255,255,0 纯黄
	public static final String Olive = "#808000";// 128,128,0 橄榄
	public static final String DarkKhaki = "#BDB76B";// 189,183,107 深卡其布
	public static final String LemonChiffon = "#FFFACD";// 255,250,205 柠檬薄纱
	public static final String PaleGodenrod = "#EEE8AA";// 238,232,170 灰秋麒麟
	public static final String Khaki = "#F0E68C";// 240,230,140 卡其布
	public static final String Gold = "#FFD700";// 255,215,0 金
	public static final String Cornislk = "#FFF8DC";// 255,248,220 玉米色
	public static final String GoldEnrod = "#DAA520";// 218,165,32 秋麒麟
	public static final String FloralWhite = "#FFFAF0";// 255,250,240 花的白色
	public static final String OldLace = "#FDF5E6";// 253,245,230 老饰带
	public static final String Wheat = "#F5DEB3";// 245,222,179 小麦色
	public static final String Moccasin = "#FFE4B5";// 255,228,181 鹿皮鞋
	public static final String Orange = "#FFA500";// 255,165,0 橙色
	public static final String PapayaWhip = "#FFEFD5";// 255,239,213 番木瓜
//	public static final String BlanchedAlmond = "#FFEBCD";// 255,235,205 漂白的杏仁
	public static final String NavajoWhite = "#FFDEAD";// 255,222,173 Navajo白
//	public static final String AntiqueWhite = "#FAEBD7";// 250,235,215 古代的白色
	public static final String Tan = "#D2B48C";// 210,180,140 晒黑
	public static final String BrulyWood = "#DEB887";// 222,184,135 结实的树
//	public static final String Bisque = "#FFE4C4";// 255,228,196 (浓汤)乳脂,番茄等
	public static final String DarkOrange = "#FF8C00";// 255,140,0 深橙色
	public static final String Linen = "#FAF0E6";// 250,240,230 亚麻布
	public static final String Peru = "#CD853F";// 205,133,63 秘鲁
	public static final String PeachPuff = "#FFDAB9";// 255,218,185 桃色
	public static final String SandyBrown = "#F4A460";// 244,164,96 沙棕色
	public static final String Chocolate = "#D2691E";// 210,105,30 巧克力
	public static final String SaddleBrown = "#8B4513";// 139,69,19 马鞍棕色
	public static final String SeaShell = "#FFF5EE";// 255,245,238 海贝壳
	public static final String Sienna = "#A0522D";// 160,82,45 黄土赭色
	public static final String LightSalmon = "#FFA07A";// 255,160,122 浅鲜肉(鲑鱼)色
	public static final String Coral = "#FF7F50";// 255,127,80 珊瑚
	public static final String OrangeRed = "#FF4500";// 255,69,0 橙红色
	public static final String DarkSalmon = "#E9967A";// 233,150,122 深鲜肉(鲑鱼)色
	public static final String Tomato = "#FF6347";// 255,99,71 番茄
	public static final String MistyRose = "#FFE4E1";// 255,228,225 薄雾玫瑰
	public static final String Salmon = "#FA8072";// 250,128,114 鲜肉(鲑鱼)色
	public static final String Snow = "#FFFAFA";// 255,250,250 雪
	public static final String LightCoral = "#F08080";// 240,128,128 淡珊瑚色
	public static final String RosyBrown = "#BC8F8F";// 188,143,143 玫瑰棕色
	public static final String IndianRed = "#CD5C5C";// 205,92,92 印度红
	public static final String Red = "#FF0000";// 255,0,0 纯红
	public static final String Brown = "#A52A2A";// 165,42,42 棕色
	public static final String FireBrick = "#B22222";// 178,34,34 耐火砖
	public static final String DarkRed = "#8B0000";// 139,0,0 深红色
	public static final String Maroon = "#800000";// 128,0,0 栗色
	public static final String White = "#FFFFFF";// 255,255,255 纯白
	public static final String WhiteSmoke = "#F5F5F5";// 245,245,245 白烟
	public static final String Gainsboro = "#DCDCDC";// 220,220,220 Gainsboro
	public static final String LightGrey = "#D3D3D3";// 211,211,211 浅灰色
	public static final String Silver = "#C0C0C0";// 192,192,192 银白色
	public static final String DarkGray = "#A9A9A9";// 169,169,169 深灰色
	public static final String Gray = "#808080";// 128,128,128 灰色
	public static final String DimGray = "#696969";// 105,105,105 暗淡的灰色
//	public static final String Black = "#000000";// 0,0,0 纯黑


	public static Map<String, Integer> getColors(String[] names) {
		Map<String, Integer> map = new HashMap<>();
		Class<ColorUtil> clazz = ColorUtil.class;
		Field[] fields = clazz.getFields();
		for (int i = 0; i < names.length; i++) {
			String cs;
			try {
				cs = fields[i%fields.length].get(clazz).toString();
				int color = Color.parseColor(cs);
//				Log.e("test", names[i] + ":" + cs);
				map.put(names[i], color);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
