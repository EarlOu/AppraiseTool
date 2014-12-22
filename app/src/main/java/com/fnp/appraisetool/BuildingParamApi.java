package com.fnp.appraisetool;

import android.util.Log;
import android.util.Xml;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BuildingParamApi {

    public static class BuildingParam {
        public int year;
        public int month;
        public double buildingParam;
        private String option;

        public BuildingParam(int y, int m, double param) {
            year = y;
            month = m;
            buildingParam = param;
        };
    }

    private static final String URL_BUILDING_PARAM_LIST
            = "http://ebas1.ebas.gov.tw/pxweb/Dialog/varval.asp?ma=PR0502A1M&ti=%C0%E7%B3y%A4u%B5{%AA%AB%BB%F9%AB%FC%BC%C6-%A4%EB&path=../PXfile/PriceStatistics/&lang=9&strList=L";

    private static final String URL_BUILDING_PARAM
            = "http://ebas1.ebas.gov.tw/pxweb/Dialog/Saveshow.asp";

    public BuildingParam getLastestParam() throws ParsingErrorException, IOException {
        BuildingParam param = parseLastestParamElement(getParamListPage());
        param.buildingParam = parseBuildingParam(getBuildingParamPage(param.option));
        return param;
    }

    private Document getParamListPage() throws IOException {
        Connection conn = Jsoup.connect(URL_BUILDING_PARAM_LIST);
        return conn.get();
    }

    private BuildingParam parseLastestParamElement(Document paramListPage) throws ParsingErrorException {
        Element select = paramListPage.select("select[name=values1]").first();
        if (select == null) throw new ParsingErrorException();
        Element last = select.children().last();
        if (last == null) throw new ParsingErrorException();

        String val = last.val();
        String lastUpdateString = last.html().trim();
        try {
            int year = Integer.parseInt(lastUpdateString.substring(0, 4));
            int month = Integer.parseInt(lastUpdateString.substring(5));
            BuildingParam res = new BuildingParam(year, month, 0);
            res.option = val;
            return res;
        } catch (NumberFormatException e) {
            throw new ParsingErrorException();
        }
    }

    private Document getBuildingParamPage(String optionValue) throws IOException {
        URL url = new URL(URL_BUILDING_PARAM);
        final String data = "strList=L&var1=%B4%C1%B6%A1&var2=%B6%B5%A5%D8&var3=%BA%D8%C3%FE&Valdavarden1=1&Valdavarden2=1&Valdavarden3=1&values1="
                + optionValue
                + "&values2=1&values3=1&context1=&begin1=&context2=&begin2=&context3=&begin3=&matrix=PR0502A1M&root=..%2FPXfile%2FPriceStatistics%2F&classdir=..%2FPXfile%2FPriceStatistics%2F&noofvar=3&elim=NNN&numberstub=1&lang=9&varparm=ma%3DPR0502A1M%26ti%3D%25C0%25E7%25B3y%25A4u%25B5%257B%25AA%25AB%25BB%25F9%25AB%25FC%25BC%25C6%252D%25A4%25EB%26path%3D%252E%252E%252FPXfile%252FPriceStatistics%252F%26xu%3D%26yp%3D%26lang%3D9&ti=%C0%E7%B3y%A4u%B5%7B%AA%AB%BB%F9%AB%FC%BC%C6-%A4%EB&infofile=&mapname=&multilang=&mainlang=&timevalvar=&hasAggregno=0&sel=%C4%7E%C4%F2&stubceller=1&headceller=1&pxkonv=asp1";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        OutputStream os = null;
        InputStream is = null;

        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Encoding", "big5");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            os = conn.getOutputStream();

            os.write(data.getBytes());
            os.flush();
            os.close();

            is = conn.getInputStream();
            return Jsoup.parse(is, "big5", URL_BUILDING_PARAM);
        } finally {
            Util.closeSliently(is);
            Util.closeSliently(os);
        }
    }


    private double parseBuildingParam(Document paramPage) throws ParsingErrorException {
        Elements tables = paramPage.select("table>tbody");
        if (tables.size() < 2) throw new ParsingErrorException();

        Element table = tables.get(1);

        if (table == null || table.children().size() < 4) throw new ParsingErrorException();
        Element tr = table.child(3);

        if (tr.children().size() < 2) throw new ParsingErrorException();
        String val = tr.child(1).html().trim();

        try {
            double value = Double.parseDouble(val);
            return value;
        } catch(NumberFormatException e) {
            throw new ParsingErrorException();
        }
    }

    public static class ParsingErrorException extends Exception {};
}
