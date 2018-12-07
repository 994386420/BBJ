package cn.kuaishang.kssdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;

public class KSEmotionUtil {

    public static final String REGEX_EMOJI = "\\[[^\\]]+\\]";
    public static final String REGEX_WEBSITE = "([a-zA-Z]+://)?[a-zA-Z\\d-]+(\\.[a-zA-Z\\d-]+)*(\\.[a-zA-Z]+)+(:\\d+)?(/[\\w\\d\\.\\-~!@#$%^&*+?:_/=<>]*)?";
    public static final String REGEX_GROUP = "(" + REGEX_EMOJI + ")|(" + REGEX_WEBSITE + ")";

    private KSEmotionUtil() {
    }

    public static final Map<String, Integer> sEmotionMap;

    public static final String[] sEmotionKeyArr = new String[]{
            "[微笑]",
            "[撇嘴]",
            "[色]",
            "[发呆]",
            "[得意]",
            "[流泪]",
            "[害羞]",
            "[闭嘴]",
            "[睡]",
            "[大哭]",
            "[尴尬]",
            "[发怒]",
            "[调皮]",
            "[呲牙]",
            "[惊讶]",
            "[难过]",
            "[酷]",
            "[冷汗]",
            "[抓狂]",
            "[吐]",
            "[偷笑]",
            "[愉快]",
            "[白眼]",
            "[傲慢]",
            "[饥饿]",
            "[困]",
            "[惊恐]",
            "[流汗]",
            "[憨笑]",
            "[悠闲]",
            "[奋斗]",
            "[咒骂]",
            "[疑问]",
            "[嘘]",
            "[晕]",
            "[疯了]",
            "[衰]",
            "[骷髅]",
            "[敲打]",
            "[再见]",
            "[擦汗]",
            "[抠鼻]",
            "[鼓掌]",
            "[糗大了]",
            "[坏笑]",
            "[左哼哼]",
            "[右哼哼]",
            "[哈欠]",
            "[鄙视]",
            "[委屈]",
            "[快哭了]",
            "[阴险]",
            "[亲亲]",
            "[吓]",
            "[可怜]",
            "[菜刀]",
            "[西瓜]",
            "[啤酒]",
            "[篮球]",
            "[乒乓]",
            "[咖啡]",
            "[饭]",
            "[猪头]",
            "[玫瑰]",
            "[凋谢]",
            "[嘴唇]",
            "[爱心]",
            "[心碎]",
            "[蛋糕]",
            "[闪电]",
            "[炸弹]",
            "[刀]",
            "[足球]",
            "[瓢虫]",
            "[便便]",
            "[月亮]",
            "[太阳]",
            "[礼物]",
            "[拥抱]",
            "[强]",
            "[弱]",
            "[握手]",
            "[胜利]",
            "[抱拳]",
            "[勾引]",
            "[拳头]",
            "[差劲]",
            "[爱你]",
            "[NO]",
            "[OK]",
            "[爱情]",
            "[飞吻]",
            "[跳跳]",
            "[发抖]",
            "[怄火]",
            "[转圈]",
            "[磕头]",
            "[回头]",
            "[跳绳]",
            "[挥手]",
            "[激动]",
            "[街舞]",
            "[献吻]",
            "[左太极]",
            "[右太极]"
    };

    public static final int[] sEmotionValueArr = new int[]{
            R.drawable.smiley_0,
            R.drawable.smiley_1,
            R.drawable.smiley_2,
            R.drawable.smiley_3,
            R.drawable.smiley_4,
            R.drawable.smiley_5,
            R.drawable.smiley_6,
            R.drawable.smiley_7,
            R.drawable.smiley_8,
            R.drawable.smiley_9,
            R.drawable.smiley_10,
            R.drawable.smiley_11,
            R.drawable.smiley_12,
            R.drawable.smiley_13,
            R.drawable.smiley_14,
            R.drawable.smiley_15,
            R.drawable.smiley_16,
            R.drawable.smiley_17,
            R.drawable.smiley_18,
            R.drawable.smiley_19,
            R.drawable.smiley_20,
            R.drawable.smiley_21,
            R.drawable.smiley_22,
            R.drawable.smiley_23,
            R.drawable.smiley_24,
            R.drawable.smiley_25,
            R.drawable.smiley_26,
            R.drawable.smiley_27,
            R.drawable.smiley_28,
            R.drawable.smiley_29,
            R.drawable.smiley_30,
            R.drawable.smiley_31,
            R.drawable.smiley_32,
            R.drawable.smiley_33,
            R.drawable.smiley_34,
            R.drawable.smiley_35,
            R.drawable.smiley_36,
            R.drawable.smiley_37,
            R.drawable.smiley_38,
            R.drawable.smiley_39,
            R.drawable.smiley_40,
            R.drawable.smiley_41,
            R.drawable.smiley_42,
            R.drawable.smiley_43,
            R.drawable.smiley_44,
            R.drawable.smiley_45,
            R.drawable.smiley_46,
            R.drawable.smiley_47,
            R.drawable.smiley_48,
            R.drawable.smiley_49,
            R.drawable.smiley_50,
            R.drawable.smiley_51,
            R.drawable.smiley_52,
            R.drawable.smiley_53,
            R.drawable.smiley_54,
            R.drawable.smiley_55,
            R.drawable.smiley_56,
            R.drawable.smiley_57,
            R.drawable.smiley_58,
            R.drawable.smiley_59,
            R.drawable.smiley_60,
            R.drawable.smiley_61,
            R.drawable.smiley_62,
            R.drawable.smiley_63,
            R.drawable.smiley_64,
            R.drawable.smiley_65,
            R.drawable.smiley_66,
            R.drawable.smiley_67,
            R.drawable.smiley_68,
            R.drawable.smiley_69,
            R.drawable.smiley_70,
            R.drawable.smiley_71,
            R.drawable.smiley_72,
            R.drawable.smiley_73,
            R.drawable.smiley_74,
            R.drawable.smiley_75,
            R.drawable.smiley_76,
            R.drawable.smiley_77,
            R.drawable.smiley_78,
            R.drawable.smiley_79,
            R.drawable.smiley_80,
            R.drawable.smiley_81,
            R.drawable.smiley_82,
            R.drawable.smiley_83,
            R.drawable.smiley_84,
            R.drawable.smiley_85,
            R.drawable.smiley_86,
            R.drawable.smiley_87,
            R.drawable.smiley_88,
            R.drawable.smiley_89,
            R.drawable.smiley_90,
            R.drawable.smiley_91,
            R.drawable.smiley_92,
            R.drawable.smiley_93,
            R.drawable.smiley_94,
            R.drawable.smiley_95,
            R.drawable.smiley_96,
            R.drawable.smiley_97,
            R.drawable.smiley_98,
            R.drawable.smiley_99,
            R.drawable.smiley_100,
            R.drawable.smiley_101,
            R.drawable.smiley_102,
            R.drawable.smiley_103,
            R.drawable.smiley_104
    };

    static {
        sEmotionMap = new HashMap<>();
        int count = sEmotionKeyArr.length;
        for (int i = 0; i < count; i++) {
            sEmotionMap.put(sEmotionKeyArr[i], sEmotionValueArr[i]);
        }
    }

    public static int getImgByName(String imgName) {
        Integer integer = sEmotionMap.get(imgName);
        return integer == null ? -1 : integer;
    }

    public static SpannableString getEmotionText(Context context, String source, int emotionSizeDp) {
        SpannableString spannableString = new SpannableString(source);
        Pattern pattern = Pattern.compile(REGEX_GROUP);
        Matcher matcher = pattern.matcher(spannableString);
        if (matcher.find()) {
            matcher.reset();
        }

        while (matcher.find()) {
            String emojiStr = matcher.group(1);
            // 处理emoji表情
            if (emojiStr != null) {
                ImageSpan imageSpan = getImageSpan(context, emojiStr, emotionSizeDp);
                if (imageSpan != null) {
                    int start = matcher.start(1);
                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }

    public static ImageSpan getImageSpan(Context context, String emojiStr, int emotionSizeDp) {
        ImageSpan imageSpan = null;
        int imgRes = getImgByName(emojiStr);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
        if (bitmap != null) {
            int size = KSUIUtil.dip2px(context, emotionSizeDp);
            bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
            //bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
            imageSpan = new ImageSpan(context, bitmap);
        }
        return imageSpan;
    }
}