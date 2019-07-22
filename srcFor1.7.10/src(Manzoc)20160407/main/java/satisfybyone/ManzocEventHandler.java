package satisfybyone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

import satisfybyone.calendarzone.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ManzocEventHandler {
	public int lastHour;
	public int lastMinute;
	public Calendar calendar = new GregorianCalendar();
	public List<CalendarZone> calendarZones = new ArrayList<CalendarZone>();
	public String[][] citiesNormal = 
		{{"カイロ(エジプト)","ケープタウン(南アフリカ)","カリーニングラード(ロシア連邦)"},
			{},{"ンジャメナ(チャド)","ブラザヴィル(コンゴ共和国)","リーブルヴィル(ガボン)","ヤウンデ(カメルーン)","ルアンダ(アンゴラ)"},
			{},{"レイキャヴィーク(アイスランド)","ヤムスクロ(コートジボワール)","ワガドゥグー(ブルキナファソ)","ヌアクショット(モーリタニア)","ブーベ島(ノルウェー)",
				"スヴァールバル諸島(ノルウェー)","セントヘレナ・アセンションおよびトリスタンダクーニャ(イギリス)","デンマークシャウン(グリーンランド)","アクラ(ガーナ)"},
			{},{},{},{"フェルナンド・デ・ノローニャ島(ブラジル)","サウスジョージア・サンドウィッチ諸島(イギリス)"},{},{"サンピエール島(フランス)","ベレン(ブラジル)",
				"フォークランド諸島(イギリス)","ブエノスアイレス(アルゼンチン)","モンテビデオ(アルゼンチン)","サンティアゴ(チリ)"},
			{},{"マナウス（ブラジル)","サントドミンゴ(ドミニカ共和国)","プエルトリコ島(アメリカ)","セントジョンズ(アンティグア・バーブーダ)",
				"キングスタウン(セントビンセントおよびグレナディーン諸島)","ポートオブスペイン(トリニダード・トバゴ)","バセテール(セントクリストファーネイビス)","ロゾー(ドミニカ国)",
				"カストリーズ(セントルシア)","ブリッジタウン(バルバトス)","セントジョージズ(グレナダ)"},
			{"カラカス(ベネズエラ)","シウダーグアジャナ(ベネズエラ)","マラカイボ(ベネズエラ)","バルキシメト(ベネズエラ)"},
			{"リオブランコ(ブラジル)","イースター島(チリ)","ボゴダ(コロンビア)","リマ(ペルー)","キト(エクアドル)","パナマシティ(パナマ)","キングストン(ジャマイカ)"},
			{},{"ガラパゴス諸島(エクアドル)","ベルモパン(ベリーズ)","グアテマラシティ(グアテマラ)","テグシガルパ(ホンジュラス)","サンホセ(コスタリカ)","レジャイナ(カナダ)"},
			{},{"フェニックス(アメリカ)"},{},{},{},{"ガンビエ諸島(仏領オセアニア)"},{"タイオハエ(仏領オセアニア)"},
			{"ホノルル(アメリカ)","パペーテ(仏領オセアニア)","ラトロンガ島(ニュージーランド)","ジョンストン島(アメリカ)","ライン諸島(キリバス)"},
			{},{"ヌクアロファ(トンガ)","パゴパゴ(アメリカ)","ニウエ島(ニュージーランド)","ミッドウェー島(アメリカ)"},{},
			{"フナフティ(ツバル)","マジュロ(マーシャル諸島)","ペトロパブロフスク・カムチャツキー(ロシア連邦)","ウェーク島(アメリカ)"},{},
			{"ホニアラ(ソロモン諸島)","ポートビラ(バヌアツ)","スレドネコリムスク(サハ共和国)"},{},
			{"サイパン(アメリカ)","ウラジオストク(ロシア連邦)","グアム(アメリカ)","オイミャコン(サハ共和国)","ポートモレスビー(パプアニューギニア)"},
			{"ダーウィン(オーストラリア)","アリススプリングス(オーストラリア)"},{},
			{"平壌(北朝鮮)","咸興(北朝鮮)","清津(北朝鮮)","南浦(北朝鮮)","元山(北朝鮮)"},
			{"マニラ(フィリピン)","北京(中国)","香港(中国)","イルクーツク(ロシア連邦)","ウラン・ウデ(ブリヤート共和国)","ハルビン(中国)"},{},
			{"バンコク(タイ)","プノンペン(カンボジア)","クラスノヤルスク(ロシア連邦)","フライング・フィッシュ・コーブ(クリスマス島オーストラリア)"},
			{"ヤンゴン(ミャンマー)","ネビドー(ミャンマー)","ココス諸島(オーストラリア)"},
			{"チッタゴン(バングラデシュ)","ビシュケク(キルギス)","ティンプー(ブータン)","アルマトイ(カザフスタン)","ノヴォシビルスク(ロシア連邦)","ダッカ(バングラデシュ)","ディエゴガルシア島(英領インド洋地域)","オムスク(ロシア連邦)"},
			{"スリジャヤワルダナプラコッテ(スリランカ)","ニューデリー(インド)","コルカタ(インド)","チェラプンジ(インド)","ムンバイ(インド)","カリカット(インド)","アフマダーバード(インド)"},
			{"タシュケント(ウズベキスタン)","エカテリンブルク(ロシア連邦)","マレ(モルディブ)","ポルトーフランセ(フランス領南方・南極地域)","サマルカンド(ウズベキスタン)",
				"イスラマバード(パキスタン)","アシガバート(トルクメニスタン)","ドゥシャンベ(タジキスタン)","ハード島とマクドナルド諸島(オーストラリア)","アクトベ(カザフスタン)"},
			{"カブール(アフガニスタン)","ザランジ(アフガニスタン)","ヘラート(アフガニスタン)","マザーリシャリーフ(アフガニスタン)","カンダハル(アフガニスタン)"},
			{"マスカット(オマーン)","アブダビ(アラブ首長国連邦)","ドバイ(アラブ首長国連邦)","イジェフスク(ウドムルト共和国)","ヴィクトリア(セイシェル)","ポートルイス(モーリシャス)",
				"トビリシ(ジョージア)","エレバン(アルメニア)","レユニオン島(フランス)","サマーラ(ロシア連邦)","バクー(アゼルバイジャン)"},
			{},{"モスクワ(ロシア連邦)","バグダード(イラク)","アンタナナリボ(マダガスカル)","ドドマ(タンザニア)","ナイロビ(ケニア)","サンクトペテルブルク(ロシア連邦)",
				"メッカ(サウジアラビア)","ドーハ(カタル)","マナーマ(バーレーン)","アディスアベバ(エチオピア)","ソチ(ロシア連邦）","ニジニー・ノヴゴロド(ロシア連邦)","メディナ(サウジアラビア)"},{}};
	public String[][] citiesNA = 
		{{},{},{},{},{},{},{},{},{},{},{},{"ニューファンドランド島(カナダ)"},{"ハリファックス(カナダ)","チューレ(グリーンランド)"},{},
			{"ケベック(カナダ)","ナッソー(バハマ)","サンサルヴァドル島(バハマ)","ポルトープランス(ハイチ)","イカルイト(カナダ)","モントリオール(カナダ)","トロント(カナダ)","オタワ(カナダ)"},
			{},{"シカゴ(アメリカ)","メキシコシティ(メキシコ)","オクラホマシティ(アメリカ)","ダラス(アメリカ)","ヒューストン(アメリカ)","ウィニペグ(カナダ)"},
			{},{"アカプルコ(メキシコ)","デンバー(アメリカ)","ソルトレークシティ(アメリカ)","エドモントン(カナダ)"},
			{},{"バンクーバー(カナダ)","ロサンゼルス(アメリカ)","シアトル(アメリカ)","ポートランド(アメリカ)","サンフランシスコ(アメリカ)","サンディエゴ(アメリカ)","ラスベガス(アメリカ)"},
			{},{"アンカレッジ(アメリカ)","ブルドーベイ(アメリカ)","ジュノー(アメリカ)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesEastEurope = 
		{{"アンカラ(トルコ)","アテネ(ギリシャ)","ヘルシンキ(フィンランド)","ソフィア(ブルガリア)","イスタンブール(トルコ)","ニコシア(キプロス)","ヴィリニュス(リトアニア)",
			"タリン(エストニア)","リガ(ラトビア)","ブガレスト(ルーマニア)"},{},{"ローマ(イタリア)","パリ(フランス)","ストックホルム(スウェーデン)","ベルリン(ドイツ)",
				"アントウェルペン(ベルギー)","アンドラ・ラ・ベリャ(アンドラ)","チューリッヒ(スイス)","ジュネーヴ(スイス)","プラハ(チェコ)","ブダペスト(ハンガリー)",
				"ブリュッセル(ベルギー)","ワルシャワ(ポーランド)","ファドゥーツ(リヒテンシュタイン)","オスロ(ノルウェー)","コペンハーゲン(デンマーク)","モナコ(モナコ)",
				"ザグレブ(クロアチア)","サラエボ(ボスニアヘルツェゴビナ)","ポトゴリツァ(モンテネグロ)","ベオグラード(セルビア)","ティラナ(アルバニア)",
				"ルクセンブルク(ルクセンブルク)","アムステルダム(オランダ)","マドリード(スペイン)"},{},
				{"ロンドン(イギリス)","リスボン(ポルトガル)"},{},{"イトコルトルミット(グリーンランド)"},{},{},{},{"ヌーク(グリーンランド)"},{},{},{},{},{},{},{},
				{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesIran = 
		{{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{"テヘラン(イラン)","マシュハド(イラン)","エスファハーン(イラン)","タブリーズ(イラン)","シーラーズ(イラン)"},{},{}};
	public String[][] citiesNZ = {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{"オークランド(ニュージーランド)","クライストチャーチ(ニュージーランド)","ハミルトン(ニュージーランド)","ウェリントン(ニュージーランド)"},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesOG = {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{"シドニー(オーストラリア)","メルボルン(オーストラリア)","キャンベラ(オーストラリア)","タスマニア島(オーストラリア)"},{"アデレード(オーストラリア)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesBR=
		{{},{},{},{},{},{},{},{},{},{},{"ブラジリア(ブラジル)","ベロオリゾンテ(ブラジル)","リオデジャネイロ(ブラジル)","サンパウロ(ブラジル)","ポルトアレグレ(ブラジル)"},
			{},{"クイアバ(ブラジル)","カンポグランデ(ブラジル)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{}};
	public String[][] citiesCU={{},{},{},{},{},{},{},{},{},{},{},{},{},{},{"ハバナ(キューバ)"},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesFiji=
		{{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{"スバ(フィジー)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesPY=
		{{},{},{},{},{},{},{},{},{},{},{},{},{"アスンシオン(パラグアイ)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesNamibia=
		{{},{},{"ウィントフック(ナミビア)","ロッシング(ナミビア)"},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesIsreal={{"エルサレム(イスラエル)","テルアビブ(イスラエル)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesSyria=
		{{"ダマスカス(シリア)","ハラブ(シリア)","ラージキーヤ(シリア)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesMongolE={{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{"ウランバートル(モンゴル)","チョイバルサン(モンゴル)","カラコルム(モンゴル)"},{},
			{"ホブド(モンゴル)","オーランゴム(モンゴル)"},{},{},{},{},{},{},{},{},{}};
	public String[][] citiesJordan=
		{{"アンマン(ヨルダン)","アカバ(ヨルダン)"},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},
			{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};


	public ManzocEventHandler() {
		/*
		 * java.util.Calendar c=new java.util.GregorianCalendar();
		 * this.calendar=new
		 * GregorianCalendar(c.get(java.util.Calendar.YEAR),c.get
		 * (java.util.Calendar.MONTH),c.get(java.util.Calendar.DAY_OF_MONTH)
		 * ,c.get
		 * (java.util.Calendar.HOUR_OF_DAY),c.get(java.util.Calendar.MINUTE
		 * ),c.get(java.util.Calendar.SECOND));
		 */
		this.lastHour = calendar.get(Calendar.HOUR_OF_DAY);
		this.lastMinute = calendar.get(Calendar.MINUTE);

		this.calendarZones.add(new NormalCalendarZone(citiesNormal));
		this.calendarZones.add(new NorthAmericaCalendarZone(citiesNA));
		this.calendarZones.add(new EastEuropeCalendarZone(citiesEastEurope));
		this.calendarZones.add(new IranCalendarZone(this.citiesIran));
		this.calendarZones.add(new NZCalendarZone(this.citiesNZ));
		this.calendarZones.add(new OGCalendarZone(this.citiesOG));
		this.calendarZones.add(new BRCalendarZone(this.citiesBR));
		this.calendarZones.add(new CubaCalendarZone(this.citiesCU));
		this.calendarZones.add(new FijiCalendarZone(this.citiesFiji));
		this.calendarZones.add(new PYCalendarZone(this.citiesPY));
		this.calendarZones.add(new NamibiaCalendarZone(this.citiesNamibia));
		this.calendarZones.add(new IsrealCalendarZone(this.citiesIsreal));
		this.calendarZones.add(new SyriaCalendarZone(this.citiesSyria));
		this.calendarZones.add(new EastMongolCalendarZone(this.citiesMongolE));
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ClientTickEvent event) {

		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int thirtyMinutes = hour * 2 + (minute / 30);
		int lastThirtyMinutes = this.lastHour * 2 + (this.lastMinute / 30);
		// System.out.println("hour:"+hour+" minute:"+minute);
		if(Minecraft.getMinecraft()!=null){
			String msg = "";
			if (thirtyMinutes != lastThirtyMinutes && (thirtyMinutes%2==0?(minute < 5):(minute>29&&minute<35))) {
				if (thirtyMinutes == 34) {
					msg = "<草彅剛> もう5時かぁ！小腹すいたなぁ";
					/*
					EntityClientPlayerMP p=Minecraft.getMinecraft().thePlayer;
					World w=Minecraft.getMinecraft().thePlayer.worldObj;
					if(p!=null&&w!=null){
						System.out.println("1");
						for (int x = MathHelper.floor_double(p.posX - 6); x < MathHelper
								.ceiling_double_int(p.posX + 6); x++) {
							for (int y = MathHelper.floor_double(p.posY - 6); y < MathHelper
									.ceiling_double_int(p.posY + 6); y++) {
								for (int z = MathHelper
										.floor_double(p.posZ - 6); z < MathHelper
										.ceiling_double_int(p.posZ + 6); z++) {
									if (w.getBlock(x, y, z) == ManzocCore.blockBar) {
										System.out.println("manzoc at "+x+","+y+","+z);
										w.playSoundEffect(
												x,
												y,
												z,
												"manzoc:manzoc.semiFullCM",
												0.7f,
												w.rand.nextFloat() * 0.05F + 0.95F);
									}
								}
							}
						}
						List<Entity> l = w
								.getEntitiesWithinAABBExcludingEntity(p,
										AxisAlignedBB.getBoundingBox(
												p.posX - 6, p.posY - 6,
												p.posZ - 6, p.posX + 6,
												p.posY + 6, p.posZ + 6));
						for (Entity e : l) {
							if (e instanceof EntityManzocCart) {
								w.playSoundAtEntity(e,
										"manzoc:manzoc.semiFullCM", 0.7f,
										w.rand.nextFloat() * 0.05F + 0.95F);
							}
						}
					}
					 */
				} else {
					List<String> cities = new ArrayList<String>();
					for (CalendarZone c : this.calendarZones) {
						cities.addAll(c.getManzocCitiesOnThirtyMinutes(calendar,
								thirtyMinutes));
					}
					if (cities.size() > 0) {
						String city = cities
								.get(new Random().nextInt(cities.size()));
						msg = ("<草彅剛> " + city + "はもう5時かぁ");
					}
				}

			} else if (thirtyMinutes == 33 && minute != this.lastMinute) {
				if (minute == 58) {
					msg = "<草彅剛> あぁまだかー";
				} else if (minute == 59) {
					msg = "<草彅剛> もうちょっとで5時だな";
				}
			}
			if (!msg.equals("")) {
				EntityClientPlayerMP p=Minecraft.getMinecraft().thePlayer;
				if(p!=null){
					p.addChatMessage(new ChatComponentText(msg));
				}
			}
		}
		this.lastHour = hour;
		this.lastMinute = minute;

	}
}
