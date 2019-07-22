package satisfybyone.calendarzone;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.minecraft.util.MathHelper;

public class MoonCalc {
	/***********************************************************************
    月齢計算 version 2.3                for JavaScript 1.1
                                        (C)opyright 1998 福原直人
    新規作成 1998.11.22
    最終更新 2000.03.20  新月直前に表示されない不具合に対処
             2000.03.22  画像ファイルをPNGに変更
                         （PNG非対応ブラウザは画像を非表示に）
                         IE3.02でのJavaScriptエラーを修正
             2002.05.02  画像の格納ディレクトリを絶対URLに
             2002.05.30  コードを一部整理
             2002.07.10  version 2.2
                         Internet Explorer の古いバージョン
                         (Version 4.72.3110.1で確認)で、EUCコード
                         混じりのJavaScriptを正しく解釈できない
                         ようなので、表示をすべて英語に。
             2002.07.13  version 2.3
                         日時を整形。
                         たとえば 9:3 のようにでていたのを 09:03 と表示。

謝辞：
  月齢計算式の元ネタは、NIFTY-Serve スペースフォーラムでFAQとして公開され
　たものです。
  で、同フォーラムのWebサイト(http://www.nifty.ne.jp/forum/fspace/)に、
  このスクリプトが採用されました。面映いというか、還元できてうれしい。

このスクリプトの利用について：
  このスクリプトは、福原直人(naohito@mb.infoweb.ne.jp)が著作権を有しています。
  このスクリプトは自由にご利用ください。
  もちろん、利用に際して自由に改造していただいても結構です。

	 ************************************************************************/


	/*
   新月日計算
   引数  　julian  ユリウス通日
   戻り値  与えられたユリウス通日に対する直前の新月日(ユリウス日)
	 */
	public static double getNewMoon(double julian) {

		double k     = Math.floor((julian - 2451550.09765) / 29.530589);
		double t     = k / 1236.85;
		double nmoon = 2451550.09765
				+ 29.530589  * k
				+  0.0001337 * t * t
				-  0.40720   * Math.sin((201.5643 + 385.8169 * k) * 0.017453292519943)
				+  0.17241   * Math.sin((2.5534 +  29.1054 * k) * 0.017453292519943);
		if (nmoon > julian) {
			nmoon = getNewMoon(julian - 1.0);
		}
		return (nmoon);         // julian - nmoonが現在時刻の月齢

	}

	public static double getMoonAge(double julian){
		return julian - getNewMoon(julian);
	}

	/*
    ユリウス通日計算
    引数　　時刻(Dateオブジェクト)
    戻り値　ユリウス通日(浮動小数点数)
	 */
	public static double getJulian(Date date) {

		return (date.getTime() / 86400000.0+2440587.5);

	}
	
	public static int getMoonPhase(int year,int month,int day)
	{
		int lp = 2551443; 
		Calendar now = new GregorianCalendar(year,month-1,day,20,35,0);						
		Calendar new_moon = new GregorianCalendar(1970, 0, 7, 20, 35, 0);
		double phase = ((now.getTimeInMillis() - new_moon.getTimeInMillis())/1000) % lp;
		return MathHelper.floor_double(phase /(24*3600)) + 1;
	}

}
