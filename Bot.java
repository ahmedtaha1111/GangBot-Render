import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.FileInputStream;
import java.lang.reflect.Member;
import java.util.*;
import java.util.stream.Collectors;

import javax.management.relation.Role;

public class Bot extends ListenerAdapter {

    private static final long NAME_CHANNEL = 1252567732973469726L; // روم طلب اسم
    private static final long ROLE_CHANNEL = 1303318565692379206L; // روم طلب إزالة أو طلب رتبة
    private static final long PERM1 = 1429303519286001775L;
    private static final long PERM2 = 1430452230552555570L;
    private static final String ROLE_LINK = "https://discord.com/channels/1152208552161718293/1303318565692379206";
    private static final String NAME_LINK = "https://discord.com/channels/1152208552161718293/1252567732973469726";
    private static final long SPECIAL_PROTECTED_ROLE = 1428856035531296768L;

    private final Map<String, Long> nameToId = new HashMap<>();
    private final Set<Long> perm1Allowed = new HashSet<>(); // صلاحيات PERM1
    private final Set<Long> perm2Allowed = new HashSet<>(); // صلاحيات PERM2

    public Bot() {
        // Populate perm2Allowed (صلاحيات الرتبة 1430452230552555570)
        perm2Allowed.add(1158155336105078814L); // 𝐆𝐀𝐍𝐆 𝐂𝐑𝐈𝐌𝐈𝐍𝐀𝐋 كريمينل
        perm2Allowed.add(1158155118676562071L); // 𝐆𝐚𝐧𝐠 𝐒𝐞𝐫𝐢𝐚𝐥 𝐊𝐢𝐥𝐥𝐞𝐫 سيريل كيلر
        perm2Allowed.add(1158154869799137280L); // 𝐆𝐚𝐧𝐠 𝐊𝐢𝐥𝐥𝐞𝐫 كيلر
        perm2Allowed.add(1158154604480041010L); // 𝐆𝐀𝐍𝐆 𝐌𝐄𝐌𝐁𝐄𝐑 ممبر
        perm2Allowed.add(1228781657620877384L); // تحت الاختبار
        perm2Allowed.add(1406086129375252542L); // 𝐃𝐞𝐚𝐭𝐡 𝐁𝐚𝐭𝐭𝐚𝐥𝐢𝐨𝐧  ديث باتليون
        perm2Allowed.add(1277643953029513318L); // 𝐁𝐚𝐝-𝐛𝐨𝐲𝐬 باد بويز
        perm2Allowed.add(1228330708192329729L); // 𝐑𝐞𝐝-𝐃𝐞𝐯𝐢𝐥𝐬 ريد دفلز
        perm2Allowed.add(1158167506897338472L); // 𝐆𝐑𝐎𝐕𝐄𝐏 قروف
        perm2Allowed.add(1430031028780531814L); // 𝐎𝐥𝐝 𝐒𝐜𝐡𝐨𝐨𝐥 اولد سكول
        perm2Allowed.add(1252364894829936773L); // 𝐕𝐀𝐆𝐎𝐒𝐏 فاقوس
        perm2Allowed.add(1392515173545148486L); // 𝐋𝐚𝐬𝐭 𝐜𝐚𝐥𝐥 لاست كول
        perm2Allowed.add(SPECIAL_PROTECTED_ROLE); // إضافة الرتبة المحمية
        perm2Allowed.add(1255177501349777509L); // ぃ𝐀𝐫𝐚𝐛 𝐏𝐮𝐫𝐞 𝐁𝐨𝐨𝐬𝐭𝐞𝐫 ღ

        // Populate perm1Allowed (صلاحيات الرتبة 1429303519286001775)
        perm1Allowed.add(1223781041244733500L); // 𝐆𝐀𝐍𝐆 𝐂𝐨𝐦𝐦𝐚𝐧𝐝𝐞𝐫 ♔ كوماندر
        perm1Allowed.add(1158157691814957076L); // 𝐆𝐀𝐍𝐆 𝐁𝐎𝐒𝐒 ✯ بوس
        perm1Allowed.add(1158155834124144670L); // 𝐆𝐀𝐍𝐆 𝐂𝐎 𝐁𝐎𝐒𝐒 ✯ كو بوس
        perm1Allowed.add(1158155815518220369L); // 𝐆𝐀𝐍𝐆 𝐌𝐀𝐍𝐀𝐆𝐄𝐑 ✯ منجر
        perm1Allowed.add(1158155656747036733L); // 𝐆𝐀𝐍𝐆 𝐂𝐎 𝐌𝐀𝐍𝐀𝐆𝐄𝐑 ✯ كو مانجر
        perm1Allowed.add(1158155501507444746L); // 𝐆𝐀𝐍𝐆 𝐀𝐃𝐕𝐈𝐒𝐎𝐑 ادفايزر
        perm1Allowed.add(1158155336105078814L); // 𝐆𝐀𝐍𝐆 𝐂𝐑𝐈𝐌𝐈𝐍𝐀𝐋 كريمينل
        perm1Allowed.add(1158155118676562071L); // 𝐆𝐚𝐧𝐠 𝐒𝐞𝐫𝐢𝐚𝐥 𝐊𝐢𝐥𝐥𝐞𝐫 سيريل كيلر
        perm1Allowed.add(1158154869799137280L); // 𝐆𝐚𝐧𝐠 𝐊𝐢𝐥𝐥𝐞𝐫 كيلر
        perm1Allowed.add(1158154604480041010L); // 𝐆𝐀𝐍𝐆 𝐌𝐄𝐌𝐁𝐄𝐑 ممبر
        perm1Allowed.add(1228781657620877384L); // تحت الاختبار
        perm1Allowed.add(1406086129375252542L); // 𝐃𝐞𝐚𝐭𝐡 𝐁𝐚𝐭𝐭𝐚𝐥𝐢𝐨𝐧  ديث باتليون
        perm1Allowed.add(1277643953029513318L); // 𝐁𝐚𝐝-𝐛𝐨𝐲𝐬 باد بويز
        perm1Allowed.add(1228330708192329729L); // 𝐑𝐞𝐝-𝐃𝐞𝐯𝐢𝐥𝐬 ريد دفلز
        perm1Allowed.add(1158167506897338472L); // 𝐆𝐑𝐎𝐕𝐄𝐏 قروف
        perm1Allowed.add(1430031028780531814L); // 𝐎𝐥𝐝 𝐒𝐜𝐡𝐨𝐨𝐥 اولد سكول
        perm1Allowed.add(1252364894829936773L); // 𝐕𝐀𝐆𝐎𝐒𝐏 فاقوس
        perm1Allowed.add(1392515173545148486L); // 𝐋𝐚𝐬𝐭 𝐜𝐚𝐥𝐥 لاست كول
        perm1Allowed.add(1298705932980715641L); // خارج الخدمة الوظيفية
        perm1Allowed.add(1431289565452304384L); // 𝑷𝑶𝑰𝑵𝑻 (1 ) 1 بوينت
        perm1Allowed.add(1431289677180305591L); // 𝑷𝑶𝑰𝑵𝑻 (2 ) 2 بوينت
        perm1Allowed.add(1431289691562315898L); // 𝑷𝑶𝑰𝑵𝑻 (3 ) 3 بوينت
        perm1Allowed.add(1431289707211264131L); // 𝑷𝑶𝑰𝑵𝑻 (4 ) 4 بوينت
        perm1Allowed.add(1431289721849511957L); // 𝑷𝑶𝑰𝑵𝑻 (5) 5 بوينت
        perm1Allowed.add(1431289734998655129L); // 𝑷𝑶𝑰𝑵𝑻 (6) 6 بوينت
        perm1Allowed.add(1431289748751913172L); // 𝑷𝑶𝑰𝑵𝑻 (7) 7 بوينت
        perm1Allowed.add(1431289765109436487L); // 𝑷𝑶𝑰𝑵𝑻 (8 ) 8 بوينت
        perm1Allowed.add(1431289784722260123L); // 𝑷𝑶𝑰𝑵𝑻 (9 ) 9 بوينت
        perm1Allowed.add(1431289801012809831L); // 𝑷𝑶𝑰𝑵𝑻 (10 ) 10 بوينت
        perm1Allowed.add(1431289815533621369L); // 𝑷𝑶𝑰𝑵𝑻 (11 ) 11 بوينت
        perm1Allowed.add(1431289655084580927L); // 𝑷𝑶𝑰𝑵𝑻 (12 ) 12 بوينت
        perm1Allowed.add(1431289640203190484L); // 𝑷𝑶𝑰𝑵𝑻 (13 ) 13 بوينت
        perm1Allowed.add(1431289622805352468L); // 𝑷𝑶𝑰𝑵𝑻 (14 ) 14 بوينت
        perm1Allowed.add(1431289601154224259L); // 𝑷𝑶𝑰𝑵𝑻 (15 ) 15 بوينت
        perm1Allowed.add(1420047641848446996L); // 「 ⛔️ تحذير شفهي وظيفي ⛔️」
        perm1Allowed.add(1420047578648809593L); // 「 ⛔️ إنذار وظيفي اول ⛔️」
        perm1Allowed.add(1428857364731396096L); // 「 ⛔️ إنذار وظيفي ثاني ⛔️」
        perm1Allowed.add(1406086445998931999L); // 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 𝐃𝐞𝐚𝐭𝐡 𝐁𝐚𝐭𝐭𝐚𝐥𝐢𝐨𝐧 منجر ديث باتليون
        perm1Allowed.add(1158161444244820070L); // 𝐁𝐚𝐝-𝐛𝐨𝐲𝐬 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 منجر باد بويز
        perm1Allowed.add(1158161440457359420L); // 𝐆𝐑𝐎𝐕𝐄 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 منجر قروف
        perm1Allowed.add(1228331294656823408L); // 𝐑𝐞𝐝-𝐃𝐞𝐯𝐢𝐥𝐬 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 منجر ريد دفلز
        perm1Allowed.add(1158161470018826300L); // 𝐋𝐚𝐬𝐭 𝐜𝐚𝐥𝐥 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 منجر لاست كول
        perm1Allowed.add(1158161447319240705L); // 𝐕𝐚𝐠𝐨𝐬 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 منجر فاقوس
        perm1Allowed.add(1405217513675034665L); // 𝐎𝐥𝐝 𝐒𝐜𝐡𝐨𝐨𝐥 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 منجر اولد سكول
        perm1Allowed.add(1427231056356053082L); // مفاوض معتمد
        perm1Allowed.add(1409926251224895570L); // مسؤول الاجازات
        perm1Allowed.add(1409925380776792064L); // مسؤول الترقيات
        perm1Allowed.add(1409925571890249779L); // مسؤول الجرد
        perm1Allowed.add(1409926577491546223L); // مسؤول التدريبات
        perm1Allowed.add(1409926700263014421L); // مسؤول الصف
        perm1Allowed.add(1409925800505118820L); // مسؤول السجل الإجرامي
        perm1Allowed.add(1409925876661096591L); // مسؤول دفتر الحضور
        perm1Allowed.add(1409925671827800145L); // مسؤول التحذيرات
        perm1Allowed.add(1158471507983483030L); // مسؤول المقابلات
        perm1Allowed.add(1409570384579137617L); // مسؤول تحذيرات العصابات
        perm1Allowed.add(1430452230552555570L); // Star نجمه (رتبة جديدة)
        perm1Allowed.add(1429303519286001775L); // Star نجمه (رتبة جديدة)
        perm1Allowed.add(1406086445998931999L); // 𝐌𝐚𝐧𝐚𝐠𝐞𝐫 𝐃𝐞𝐚𝐭𝐡 𝐁𝐚𝐭𝐭𝐚𝐥𝐢𝐨𝐧 منجر كتيبه
        perm1Allowed.add(1255177501349777509L); // ぃ𝐀𝐫𝐚𝐛 𝐏𝐮𝐫𝐞 𝐁𝐨𝐨𝐬𝐭𝐞𝐫 ღ

        // Populate nameToId
        nameToId.put("gang commander".toLowerCase(), 1223781041244733500L);
        nameToId.put("كوماندر", 1223781041244733500L);
        nameToId.put("gang boss".toLowerCase(), 1158157691814957076L);
        nameToId.put("بوس", 1158157691814957076L);
        nameToId.put("gang co boss".toLowerCase(), 1158155834124144670L);
        nameToId.put("كو بوس", 1158155834124144670L);
        nameToId.put("gang manager".toLowerCase(), 1158155815518220369L);
        nameToId.put("منجر", 1158155815518220369L);
        nameToId.put("gang co manager".toLowerCase(), 1158155656747036733L);
        nameToId.put("كو مانجر", 1158155656747036733L);
        nameToId.put("gang advisor".toLowerCase(), 1158155501507444746L);
        nameToId.put("ادفايزر", 1158155501507444746L);
        nameToId.put("gang criminal".toLowerCase(), 1158155336105078814L);
        nameToId.put("كريمينل", 1158155336105078814L);
        nameToId.put("gang serial killer".toLowerCase(), 1158155118676562071L);
        nameToId.put("سيريل كيلر", 1158155118676562071L);
        nameToId.put("gang killer".toLowerCase(), 1158154869799137280L);
        nameToId.put("كيلر", 1158154869799137280L);
        nameToId.put("gang member".toLowerCase(), 1158154604480041010L);
        nameToId.put("ممبر", 1158154604480041010L);
        nameToId.put("under test".toLowerCase(), 1228781657620877384L);
        nameToId.put("تحت الاختبار", 1228781657620877384L);
        nameToId.put("death battalion".toLowerCase(), 1406086129375252542L);
        nameToId.put("كتيبه", 1406086129375252542L);
        nameToId.put("bad boys".toLowerCase(), 1277643953029513318L);
        nameToId.put("باد بويز", 1277643953029513318L);
        nameToId.put("red devils".toLowerCase(), 1228330708192329729L);
        nameToId.put("ريد دفلز", 1228330708192329729L);
        nameToId.put("grove".toLowerCase(), 1158167506897338472L);
        nameToId.put("قروف", 1158167506897338472L);
        nameToId.put("old school".toLowerCase(), 1430031028780531814L);
        nameToId.put("اولد سكول", 1430031028780531814L);
        nameToId.put("vagos".toLowerCase(), 1252364894829936773L);
        nameToId.put("فاقوس", 1252364894829936773L);
        nameToId.put("last call".toLowerCase(), 1392515173545148486L);
        nameToId.put("لاست كول", 1392515173545148486L);
        nameToId.put("out of functional service".toLowerCase(), 1298705932980715641L);
        nameToId.put("خارج الخدمة الوظيفية", 1298705932980715641L);
        nameToId.put("point 1".toLowerCase(), 1431289565452304384L);
        nameToId.put("1 بوينت", 1431289565452304384L);
        nameToId.put("point 2".toLowerCase(), 1431289677180305591L);
        nameToId.put("2 بوينت", 1431289677180305591L);
        nameToId.put("point 3".toLowerCase(), 1431289691562315898L);
        nameToId.put("3 بوينت", 1431289691562315898L);
        nameToId.put("point 4".toLowerCase(), 1431289707211264131L);
        nameToId.put("4 بوينت", 1431289707211264131L);
        nameToId.put("point 5".toLowerCase(), 1431289721849511957L);
        nameToId.put("5 بوينت", 1431289721849511957L);
        nameToId.put("point 6".toLowerCase(), 1431289734998655129L);
        nameToId.put("6 بوينت", 1431289734998655129L);
        nameToId.put("point 7".toLowerCase(), 1431289748751913172L);
        nameToId.put("7 بوينت", 1431289748751913172L);
        nameToId.put("point 8".toLowerCase(), 1431289765109436487L);
        nameToId.put("8 بوينت", 1431289765109436487L);
        nameToId.put("point 9".toLowerCase(), 1431289784722260123L);
        nameToId.put("9 بوينت", 1431289784722260123L);
        nameToId.put("point 10".toLowerCase(), 1431289801012809831L);
        nameToId.put("10 بوينت", 1431289801012809831L);
        nameToId.put("point 11".toLowerCase(), 1431289815533621369L);
        nameToId.put("11 بوينت", 1431289815533621369L);
        nameToId.put("point 12".toLowerCase(), 1431289655084580927L);
        nameToId.put("12 بوينت", 1431289655084580927L);
        nameToId.put("point 13".toLowerCase(), 1431289640203190484L);
        nameToId.put("13 بوينت", 1431289640203190484L);
        nameToId.put("point 14".toLowerCase(), 1431289622805352468L);
        nameToId.put("14 بوينت", 1431289622805352468L);
        nameToId.put("point 15".toLowerCase(), 1431289601154224259L);
        nameToId.put("15 بوينت", 1431289601154224259L);
        nameToId.put("verbal functional warning".toLowerCase(), 1420047641848446996L);
        nameToId.put("تحذير شفهي وظيفي", 1420047641848446996L);
        nameToId.put("first functional warning".toLowerCase(), 1420047578648809593L);
        nameToId.put("إنذار وظيفي اول", 1420047578648809593L);
        nameToId.put("second functional warning".toLowerCase(), 1428857364731396096L);
        nameToId.put("إنذار وظيفي ثاني", 1428857364731396096L);
        nameToId.put("special protected role".toLowerCase(), SPECIAL_PROTECTED_ROLE);
        nameToId.put("رتبة محمية", SPECIAL_PROTECTED_ROLE);
        nameToId.put("manager death battalion".toLowerCase(), 1406086445998931999L);
        nameToId.put("منجر كتيبه", 1406086445998931999L);
        nameToId.put("bad boys manager".toLowerCase(), 1158161444244820070L);
        nameToId.put("منجر باد بويز", 1158161444244820070L);
        nameToId.put("grove manager".toLowerCase(), 1158161440457359420L);
        nameToId.put("منجر قروف", 1158161440457359420L);
        nameToId.put("red devils manager".toLowerCase(), 1228331294656823408L);
        nameToId.put("منجر ريد دفلز", 1228331294656823408L);
        nameToId.put("last call manager".toLowerCase(), 1158161470018826300L);
        nameToId.put("منجر لاست كول", 1158161470018826300L);
        nameToId.put("vagos manager".toLowerCase(), 1158161447319240705L);
        nameToId.put("منجر فاقوس", 1158161447319240705L);
        nameToId.put("old school manager".toLowerCase(), 1405217513675034665L);
        nameToId.put("منجر اولد سكول", 1405217513675034665L);
        nameToId.put("certified negotiator".toLowerCase(), 1427231056356053082L);
        nameToId.put("مفاوض معتمد", 1427231056356053082L);
        nameToId.put("leave manager".toLowerCase(), 1409926251224895570L);
        nameToId.put("مسؤول الاجازات", 1409926251224895570L);
        nameToId.put("promotion manager".toLowerCase(), 1409925380776792064L);
        nameToId.put("مسؤول الترقيات", 1409925380776792064L);
        nameToId.put("inventory manager".toLowerCase(), 1409925571890249779L);
        nameToId.put("مسؤول الجرد", 1409925571890249779L);
        nameToId.put("training manager".toLowerCase(), 1409926577491546223L);
        nameToId.put("مسؤول التدريبات", 1409926577491546223L);
        nameToId.put("class manager".toLowerCase(), 1409926700263014421L);
        nameToId.put("مسؤول الصف", 1409926700263014421L);
        nameToId.put("criminal record manager".toLowerCase(), 1409925800505118820L);
        nameToId.put("مسؤول السجل الإجرامي", 1409925800505118820L);
        nameToId.put("attendance manager".toLowerCase(), 1409925876661096591L);
        nameToId.put("مسؤول دفتر الحضور", 1409925876661096591L);
        nameToId.put("warning manager".toLowerCase(), 1409925671827800145L);
        nameToId.put("مسؤول التحذيرات", 1409925671827800145L);
        nameToId.put("interview manager".toLowerCase(), 1158471507983483030L);
        nameToId.put("مسؤول المقابلات", 1158471507983483030L);
        nameToId.put("gang warnings manager".toLowerCase(), 1409570384579137617L);
        nameToId.put("مسؤول تحذيرات العصابات", 1409570384579137617L);
        nameToId.put("star".toLowerCase(), 1430452230552555570L);
        nameToId.put("نجمه", 1430452230552555570L);
        nameToId.put("lmkn".toLowerCase(), 1429303519286001775L);
        nameToId.put("نقطه", 1429303519286001775L);
        nameToId.put("ديث باتليون", 1406086129375252542L);
        nameToId.put("manager death battalion".toLowerCase(), 1406086445998931999L);
        nameToId.put("منجر ديث باتليون ", 1406086445998931999L);
        nameToId.put("verbal functional warning".toLowerCase(), 1420047641848446996L);
        nameToId.put("شفوي", 1420047641848446996L);
        nameToId.put("first functional warning".toLowerCase(), 1420047578648809593L);
        nameToId.put("تحذير اول", 1420047578648809593L);
        nameToId.put("second functional warning".toLowerCase(), 1428857364731396096L);
        nameToId.put("تحذير ثاني", 1428857364731396096L);
        nameToId.put("verbal functional warning".toLowerCase(), 1420047641848446996L);
        nameToId.put("تحذير شفهي", 1420047641848446996L);
        nameToId.put("first functional warning".toLowerCase(), 1420047578648809593L);
        nameToId.put(" اول", 1420047578648809593L);
        nameToId.put("second functional warning".toLowerCase(), 1428857364731396096L);
        nameToId.put(" ثاني", 1428857364731396096L);
        nameToId.put("verbal functional warning".toLowerCase(), 1420047641848446996L);
        nameToId.put(" شفهي", 1420047641848446996L);
    }

    public static void main(String[] args) {
        try {
                  Properties props = new Properties();
        props.load(new FileInputStream("/app/.env"));
        String token = props.getProperty("DISCORD_TOKEN");

        if (token == null || token.isEmpty()) {
            System.err.println("التوكن مش موجود في .env!");
            return;
        }

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new Bot())
                .build();
        jda.awaitReady();
        System.out.println("البوت شغال وجاهز!");
        } catch (Exception e) {
            System.err.println("خطأ في تشغيل البوت: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("رسالة وصلت من: " + event.getAuthor().getName() +
                " في قناة: " + event.getChannel().getId() +
                " المحتوى: " + event.getMessage().getContentRaw());

        if (!event.isFromGuild() || event.getAuthor().isBot()) {
            System.out.println("تم تجاهل الرسالة: ليست من سيرفر أو من بوت");
            return;
        }

        long channelId = event.getChannel().asTextChannel().getIdLong();
        System.out.println("ID القناة: " + channelId);

        if (channelId == NAME_CHANNEL) {
            handleNameChange(event);
        } else if (channelId == ROLE_CHANNEL) {
            handleRole(event);
        } else {
            System.out.println("الرسالة في قناة غير مدعومة: " + channelId);
        }
    }

    private void handleNameChange(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        System.out.println("معالجة طلب تغيير اسم: " + content);

        if (content.contains("رتبه") || content.contains("رتب") || content.contains("ازاله")) {
            message.reply("الرجاء التوجه الي " + ROLE_LINK).queue();
            System.out.println("تم إعادة توجيه الطلب إلى قناة الرتب");
            return;
        }

        List<User> mentionedUsers = message.getMentions().getUsers();
        if (mentionedUsers.size() != 1) {
            System.out.println("عدد المستخدمين المذكورين غير صحيح: " + mentionedUsers.size());
            return;
        }

        User targetUser = mentionedUsers.get(0);
        if (targetUser.getIdLong() == event.getAuthor().getIdLong()) {
            message.reply("ممنوع طلب لنفسك").queue();
            System.out.println("محاولة طلب لنفس المستخدم");
            return;
        }

        String newName = content.replaceAll("<@!?\\d+>", "").trim();
        if (newName.isEmpty()) {
            System.out.println("الاسم الجديد فارغ");
            return;
        }

        boolean hasArabic = newName.chars().anyMatch(c -> c >= 0x0600 && c <= 0x06FF);
        if (hasArabic) {
            message.reply("ممنوع الاسم بالعربي").queue();
            System.out.println("الاسم يحتوي على حروف عربية");
            return;
        }

        Guild guild = event.getGuild();
        Member requester = event.getMember();
        Member target = guild.getMember(targetUser);
        if (target == null) {
            guild.retrieveMember(targetUser).queue(
                    member -> handleNameChangeWithMember(event, member, newName, guild, requester),
                    error -> {
                        message.reply("المستخدم المستهدف غير موجود في السيرفر").queue();
                        System.err.println("فشل في جلب العضو: " + error.getMessage());
                    }
            );
            return;
        }

        handleNameChangeWithMember(event, target, newName, guild, requester);
    }

    private void handleNameChangeWithMember(MessageReceivedEvent event, Member target, String newName, Guild guild, Member requester) {
        boolean hasPerm1 = requester.getRoles().stream().anyMatch(r -> r.getIdLong() == PERM1);
        boolean hasPerm2 = requester.getRoles().stream().anyMatch(r -> r.getIdLong() == PERM2);
        if (!hasPerm1 && !hasPerm2) {
            event.getMessage().reply("ليس لديك صلاحية").queue();
            System.out.println("المستخدم ليس لديه صلاحية");
            return;
        }

        Set<Long> allowed = hasPerm1 ? perm1Allowed : perm2Allowed;
        boolean hasUnauthorizedRole = target.getRoles().stream().anyMatch(r -> !allowed.contains(r.getIdLong()) && r.getIdLong() != SPECIAL_PROTECTED_ROLE);
        if (hasUnauthorizedRole) {
            event.getMessage().reply("ممنوع بسبب رتب الشخص").queue();
            System.out.println("الشخص المستهدف لديه رتب غير مصرح بها");
            return;
        }

        guild.modifyNickname(target, newName).queue(
                success -> event.getMessage().reply("```diff\n-تـم تـحـديـث الاسم-\n```").queue(),
                error -> {
                    event.getMessage().reply("خطأ في تغيير الاسم: " + error.getMessage()).queue();
                    System.err.println("خطأ في تغيير الاسم: " + error.getMessage());
                }
        );
        System.out.println("تم تغيير الاسم إلى: " + newName);
    }

    private void handleRole(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        System.out.println("معالجة طلب رتبة: " + content);

        if (content.contains("اسم")) {
            message.reply("الرجاء التوجه الي " + NAME_LINK).queue();
            System.out.println("تم إعادة توجيه الطلب إلى قناة الأسماء");
            return;
        }

        List<User> mentionedUsers = message.getMentions().getUsers();
        if (mentionedUsers.size() != 1) {
            System.out.println("عدد المستخدمين المذكورين غير صحيح: " + mentionedUsers.size());
            return;
        }

        User targetUser = mentionedUsers.get(0);
        if (targetUser.getIdLong() == event.getAuthor().getIdLong()) {
            message.reply("ممنوع طلب لنفسك").queue();
            System.out.println("محاولة طلب لنفس المستخدم");
            return;
        }

        String afterMention = content.replaceAll("<@!?\\d+>", "").trim().replaceAll("\\s+", " ");
        if (afterMention.isEmpty()) {
            System.out.println("المحتوى بعد المنشن فارغ");
            return;
        }

        Guild guild = event.getGuild();
        Member requester = event.getMember();
        Member target = guild.getMember(targetUser);
        if (target == null) {
            guild.retrieveMember(targetUser).queue(
                    member -> handleRoleWithMember(event, member, afterMention, guild, requester),
                    error -> {
                        message.reply("المستخدم المستهدف غير موجود في السيرفر").queue();
                        System.err.println("فشل في جلب العضو: " + error.getMessage());
                    }
            );
            return;
        }

        handleRoleWithMember(event, target, afterMention, guild, requester);
    }

    private void handleRoleWithMember(MessageReceivedEvent event, Member target, String afterMention, Guild guild, Member requester) {
        boolean hasPerm1 = requester.getRoles().stream().anyMatch(r -> r.getIdLong() == PERM1);
        boolean hasPerm2 = requester.getRoles().stream().anyMatch(r -> r.getIdLong() == PERM2);
        if (!hasPerm1 && !hasPerm2) {
            event.getMessage().reply("ليس لديك صلاحية").queue();
            System.out.println("المستخدم ليس لديه صلاحية");
            return;
        }

        Set<Long> allowed = hasPerm1 ? perm1Allowed : perm2Allowed;
        boolean hasUnauthorizedRole = target.getRoles().stream().anyMatch(r -> !allowed.contains(r.getIdLong()) && r.getIdLong() != SPECIAL_PROTECTED_ROLE);
        if (hasUnauthorizedRole) {
            event.getMessage().reply("ممنوع بسبب رتب الشخص").queue();
            System.out.println("الشخص المستهدف لديه رتب غير مصرح بها");
            return;
        }

        String[] parts = afterMention.split(" ");
        if (parts.length > 0 && parts[0].equals("ازاله")) {
            if (parts.length >= 3 && parts[1].equals("جميع") && parts[2].equals("الرتب")) {
                for (Role role : target.getRoles()) {
                    long roleId = role.getIdLong();
                    if (roleId != SPECIAL_PROTECTED_ROLE) { // حماية الرتبة من الإزالة
                        guild.removeRoleFromMember(target, role).queue(
                                success -> {},
                                error -> System.err.println("خطأ في إزالة الرتبة: " + error.getMessage())
                        );
                    }
                }
                event.getMessage().reply("```diff\n- تـم تـحـديـث الرتب -\n```").queue();
                System.out.println("تم إزالة جميع الرتب عدا الرتبة المحمية");
            } else {
                String roleName = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
                Long roleId = findRoleId(roleName);
                if (roleId == null || !allowed.contains(roleId) || roleId == SPECIAL_PROTECTED_ROLE) {
                    event.getMessage().reply("رتبه غير موجودة أو غير مصرحة").queue();
                    System.out.println("الرتبة غير موجودة أو غير مصرحة: " + roleName);
                    return;
                }
                Role role = guild.getRoleById(roleId);
                if (role != null) {
                    guild.removeRoleFromMember(target, role).queue(
                            success -> {},
                            error -> System.err.println("خطأ في إزالة الرتبة: " + error.getMessage())
                    );
                }
                event.getMessage().reply("```diff\n- تـم تـحـديـث الرتب -\n```").queue();
                System.out.println("تم إزالة الرتبة: " + roleName);
            }
        } else {
            String[] roleNames = afterMention.split("\\+");
            List<Long> toAdd = new ArrayList<>();
            for (String rn : roleNames) {
                String trimmed = rn.trim();
                if (!trimmed.isEmpty()) {
                    Long roleId = findRoleId(trimmed);
                    if (roleId != null && allowed.contains(roleId)) {
                        toAdd.add(roleId);
                    }
                }
            }
            if (toAdd.isEmpty()) {
                event.getMessage().reply("رتب غير موجودة أو غير مصرحة").queue();
                System.out.println("لا توجد رتب صالحة للإضافة");
                return;
            }
            for (Long roleId : toAdd) {
                Role role = guild.getRoleById(roleId);
                if (role != null) {
                    guild.addRoleToMember(target, role).queue(
                            success -> {},
                            error -> System.err.println("خطأ في إضافة الرتبة: " + error.getMessage())
                    );
                }
            }
            event.getMessage().reply("```diff\n- تـم تـحـديـث الرتب -\n```").queue();
            System.out.println("تم إضافة الرتب: " + toAdd);
        }
    }

    private Long findRoleId(String name) {
        String norm = name.trim().toLowerCase();
        if (nameToId.containsKey(norm)) {
            System.out.println("تم العثور على الرتبة: " + norm);
            return nameToId.get(norm);
        }

        Long bestId = null;
        int minDist = Integer.MAX_VALUE;
        int countMin = 0;
        for (Map.Entry<String, Long> entry : nameToId.entrySet()) {
            String key = entry.getKey();
            int dist = levenshtein(norm, key);
            if (dist < minDist) {
                minDist = dist;
                bestId = entry.getValue();
                countMin = 1;
            } else if (dist == minDist) {
                countMin++;
            }
        }
        if (minDist <= 2 && countMin == 1) {
            System.out.println("تم العثور على الرتبة عبر البحث الغامض: " + norm + " -> " + bestId);
            return bestId;
        }
        System.out.println("لم يتم العثور على الرتبة: " + norm);
        return null;
    }

    private static int levenshtein(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];
        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else dp[i][j] = min(
                            dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
            }
        }
        return dp[x.length()][y.length()];
    }

    private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
}