package com.fuyou.play.util.HORUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class HORUtils {


    private int  year, month, day, hour, minute, day_light, timezone;
    private double gmt, longitude, latitude;
    private int julian;
    public double t;
    private double xa, ya, za, ab, zw, x1, z1, y_1, ss, p;
    private double cusp1, cusp2, temp, k, span;
    private double[] house ;
    private int[] inhouse;
    private  double[][] planetDatas;
    private  double[] _planetDatas;
    private List<HORAspect> _aspects;

    public HORUtils(BornInfo info){
        //设置用户数据
        year = info.getYear();
        month = info.getMonth();
        day = info.getDay();
        hour = info.getHour();
        minute = info.getMinute();
        day_light = info.getDaylight();
        timezone = info.getTimezone();
        latitude = info.getLat();
        if (latitude>66.66) latitude = 66.66;
        longitude = info.getLon();
        //初始化
        init();
    }

    private void init(){
        double[] pdata0 = {358.475800, 35999.049800,-0.000200, 0.016750, -0.000040, 0, 1, 101.220800, 1.719200, 0.000450, 0, 0, 0, 0, 0};
        double[] pdata1 = new double[15];
        double[] pdata2 = {102.279400, 149472.515000, 0, 0.205614, 0.000020, 0, 0.387100, 28.753800, 0.370300, 0.000100, 47.145900, 1.185200, 0.000200, 7.009000, 0.001860};
        double[] pdata3 = {212.603200, 58517.803900, 0.001300, 0.006820, 0.000050, 0, 0.723300, 54.384200, 0.508200, -0.001400, 75.779600, 0.899900, 0.000400, 3.393600, 0.001000};
        double[] pdata4 = {319.529400, 19139.858500, 0.000200, 0.093310, 0.000090, 0, 1.523700, 285.431800, 1.069800, 0.000100, 48.786400, 0.770990, 0, 1.850300, -0.000700};
        double[] pdata5 = {225.492800, 3033.687900, 0, 0.048380, -0.000020, 0,5.202900, 273.393000, 1.338300, 0, 99.419800, 1.058300, 0, 1.309700, -0.005200};
        double[] pdata6 = {174.215300, 1223.507960, 0, 0.054230, -0.000200, 0, 9.552500, 338.911700, -0.316700, 0, 112.826100, 0.825900, 0, 2.490800, -0.004700};
        double[] pdata7 = {74.175700, 427.274200, 0, 0.046820, 0.000420, 0, 19.221500, 95.686300, 2.050800, 0, 73.522200, 0.524200, 0, 0.772600, 0.000100};
        double[] pdata8 = {30.132940, 240.455160, 0, 0.009130, -0.001270, 0, 30.113750, 284.168300, -21.632900, 0, 130.684150, 1.100500, 0, 1.779400, -0.009800};
        double[] pdata9 = {229.947220, 144.913060, 0, 0.248640, 0, 0, 39.517740, 113.521390, 0, 0, 108.954440, 1.396010, 0.000310, 17.146780, 0};
        planetDatas = new double[][]{pdata0, pdata1 ,pdata2, pdata3, pdata4, pdata5, pdata6, pdata7, pdata8, pdata9};
        house = new double[12];
        inhouse  = new int[10];
        _planetDatas = new double[10];
    }

    public HORChartResult calculateWithBornInfo(){
        calculateGMTandLocation();
        calculatePlanetLocations();
        calculateAspects();
        HORChartResult result = new HORChartResult();
        result.setAspectDatas(_aspects);
        result.setHouseDatas(calculateHouseDatas());
        result.setPlanetDatas(calculatePlanetDatas());
        return result;
    }

    private double rem(double a, int b){
        long c = (long)a;
        double offset = a - c;
        long d = c % b;
        return d + offset;
    }

    private double polara (double x, double y)
    {
        double a = Math.atan(y / x);
        if (a < 0) {
            a += 3.141593;
        }
        if (y < 0) {
            a += 3.141593;
        }
        return a;
    }

    private double polarr(double x, double y)
    {
        return (Math.sqrt(x * x + y * y));
    }

    private double dsin(double degree)
    {
        return Math.sin(0.017453 * degree);
    }

    private double acs(double a)
    {
        return (Math.atan(Math.sqrt(1 - a * a) / a));
    }

    private double plac(double ff, double y, double r1, double ra, double ob, double la)
    {
        double x = -1;
        if (y == 1)
        {
            x = 1;
        } // end if
        for (int i = 1; i < 11; i++)
        {
            double xx = acs(x * Math.sin(r1) * Math.tan(ob) * Math.tan(0.017453 * la));
            if (xx < 0)
            {
                xx = xx + 3.141593;
            } // end if
            double r2 = ra + xx / ff;
            if (y == 1)
            {
                r2 = ra + 3.141593 - xx / ff;
            } // end if
            r1 = r2;
        } // end of for
        double lo = Math.atan(Math.tan(r1) / Math.cos(ob));
        if (lo < 0)
        {
            lo = lo + 3.141593;
        } // end if
        if (Math.sin(r1) < 0)
        {
            lo = lo + 3.141593;
        } // end if
        return (57.295780 * lo);
    }

    private void calculateGMTandLocation()
    {
        if (year < 1900 || year > 2300 || latitude > 66.666667)
        {
            return;
        }
        double im = 12 * (year + 4800) + (month - 3);
        julian = (int)((2 * (im - (int)(im / 12) * 12) + 7 + 365 * im) / 12);

        julian = (int)(julian + day + (int)(im / 48) - 32083);
        if (julian > 2299171)
        {
            julian = julian + (int)(im / 4800) - (int)(im / 1200) + 38;
        }
        gmt = hour + (float)minute / 60 + timezone - day_light;
        if (gmt < 0)
        {
            gmt = gmt + 24;
            julian = julian - 1;
        }
        if (gmt > 24)
        {
            gmt = gmt - 24;
            julian = julian + 1;
        }
    }

    private void calculatePlanetLocations() {
        t = (julian - 2415020 + (gmt / 24 - 0.500000)) / 36525;
        for (int i = 0; i<10; i++) {
            if (i == 1) {
                double da = t * 36525;
                double ln = rem(259.1833 - 0.052954 * da + (0.000002 * t + 0.002078) * t * t, 360);
                double ms = rem(279.69668 + 36000.7689 * t + 0.000302 * t * t, 360);
                double de = rem(350.737486 + 445267.114 * t - 0.001436 * t * t, 360);
                double lp = rem(334.329560 + 0.111404 * da + (-0.000012 * t + 0.010325) * t * t, 360);
                double ma = rem(358.475840 + 35999.049750 * t - 0.000150 * t * t, 360);
                double ml = rem(270.434164 + 13.176396 * da + (0.000002 * t - 0.001133) * t * t, 360);
                double nu = (-(17.232700 + 0.017370 * t) * dsin(ln) - 1.273000 * dsin(2 * ms)) / 3600;
                double el = rem(296.104608 + 477198.849000 * t + 0.009192 * t * t, 360);
                el = ma;
                double ll = ml - lp;
                double ff = ml - ln;
                double w = dsin(51.200000 + 20.200000 * t);
                double x = dsin(193.440400 - 132.870000 * t - 0.009173 * t * t) * 14.270000;
                double y = dsin(ln);
                double z = -15.580000 * dsin(ln + 275.050000 - 2.300000 * t);
                ml = (0.840000 * w + x + 7.261000 * y) / 3600 + ml;
                ll = (2.940000 * w + x + 9.337000 * y) / 3600 + ll;
                de = (7.240000 * w + x + 7.261000 * y) / 3600 + de;
                el = -6.400000 * w / 3600 + el;
                ff = (0.210000 * w + x - 88.699000 * y - 15.580000 * z) / 3600 + ff;
                double l = 22639.550000 * dsin(ll) - 4586.470000 * dsin(ll - 2 * de) + 2369.912000 * dsin(2 * de);
                l = l + 769.020000 * dsin(2 * ll) - 668.150000 * dsin(el);
                l = l - 411.610000 * dsin(2 * ff) - 211.660000 * dsin(2 * ll - 2 * de) - 205.960000 * dsin(ll + el - 2 * de);
                l = l + 191.950000 * dsin(ll + 2 * de) - 165.150000 * dsin(el - 2 * de);
                l = l + 147.690000 * dsin(ll - el) - 125.150000 * dsin(de) - 109.670000 * dsin(ll + el);
                l = l - 55.170000 * dsin(2 * ff - 2 * de) - 45.099000 * dsin(ll + 2 * ff);
                l = l + 39.530000 * dsin(ll - 2 * ff) - 38.430000 * dsin(ll - 4 * de) + 36.120000 * dsin(3 * ll);
                l = l - 30.770000 * dsin(2 * ll - 4 * de) + 28.480000 * dsin(ll - el - 2 * de);
                l = l - 24.420000 * dsin(el + 2 * de);
                l = l + 18.610000 * dsin(ll - de) + 18.020000 * dsin(el + de) + 14.580000 * dsin(ll - el + 2 * de);
                l = l + 14.390000 * dsin(2 * ll + 2 * de) + 13.900000 * dsin(4 * de) - 13.190000 * dsin(3 * ll - 2 * de);
                l = l + 9.700000 * dsin(2 * ll - el) + 9.370000 * dsin(ll - 2 * ff - 2 * de) - 8.630000 * dsin(2 * ll + el - 2 * de);
                l = l - 8.470000 * dsin(ll + de) - 8.096000 * dsin(2 * el - 2 * de) - 7.650000 * dsin(2 * ll + el);
                l = l - 7.490000 * dsin(2 * el) - 7.410000 * dsin(ll + 2 * el - 2 * de) - 6.380000 * dsin(ll - 2 * ff + 2 * de);
                l = l - 5.740000 * dsin(2 * ff + 2 * de) - 4.390000 * dsin(ll + el - 4 * de) - 3.990000 * dsin(2 * ll + 2 * ff);
                l = l + 3.220000 * dsin(ll - 3 * de) - 2.920000 * dsin(ll + el + 2 * de) - 2.740000 * dsin(2 * ll + el - 4 * de);
                l = l - 2.490000 * dsin(2 * ll - el - 2 * de) + 2.580000 * dsin(ll - 2 * el) + 2.530000 * dsin(ll - 2 * el - 2 * de);
                l = l - 2.150000 * dsin(el + 2 * ff - 2 * de) + 1.980000 * dsin(ll + 4 * de) + 1.940000 * dsin(4 * ll);
                l = l - 1.880000 * dsin(el - 4 * de) + 1.750000 * dsin(2 * ll - de) - 1.440000 * dsin(el - 2 * ff + 2 * de);
                l = l - 1.298000 * dsin(2 * ll - 2 * ff) - 1.270000 * dsin(ll + el + de) + 1.230000 * dsin(2 * ll - 3 * de);
                l = l - 1.190000 * dsin(3 * ll - 4 * de) + 1.180000 * dsin(2 * ll - el + 2 * de) - 1.170000 * dsin(ll + 2 * el);
                l = l - 1.090000 * dsin(ll - el - de) + 1.060000 * dsin(3 * ll + 2 * de) - 0.590000 * dsin(2 * ll + de);
                l = l - 0.990000 * dsin(ll + 2 * ff + 2 * de) - 0.950000 * dsin(4 * ll - 2 * de) - 0.570000 * dsin(2 * ll - 6 * de);
                l = l + 0.640000 * dsin(ll - 4 * de) + 0.560000 * dsin(el - de) + 0.760000 * dsin(ll - 2 * el + 2 * de);
                l = l + 0.580000 * dsin(2 * ff - de) - 0.550000 * dsin(3 * ll + el) + 0.680000 * dsin(3 * ll - el);
                l = (l + 0.557000 * dsin(2 * ll + 2 * ff - 2 * de) + 0.538000 * dsin(2 * ll - 2 * ff - 2 * de)) / 3600;
//                self.planetDatas[1] = @(rem(ml + l + nu, 360));
                _planetDatas[1] = rem(ml + l + nu, 360);
                i = 2;
            }
            double m = 0.017453 * (rem(planetDatas[i][0] + planetDatas[i][1] * t + planetDatas[i][2]* t * t, 360));
            double e = planetDatas[i][3] + planetDatas[i][4] * t + planetDatas[i][5] * t * t;
            double ea = m;
            for (int a = 1; a < 5; a++)
            {
                ea = m + e * Math.sin(ea);
            } // end of for
            double au = planetDatas[i][6];
            double e1 = 0.017202 / (Math.pow(au, 1.500000) * (1 - e * Math.cos(ea)));
            double xw = -au * e1 * Math.sin(ea);
            double yw = au * e1 * Math.pow(Math.abs(1 - e * e), 0.500000) * Math.cos(ea);
            double ap = 0.017453 * (planetDatas[i][7] + planetDatas[i][8] * t + planetDatas[i][9] * t * t);
            double an = 0.017453 * (planetDatas[i][10] + planetDatas[i][11] * t + planetDatas[i][12] * t * t);
            double i_n = 0.017453 * (planetDatas[i][13] + planetDatas[i][14] * t);
            double x = xw;
            double y = yw;
            if (y == 0)
            {
                y = 0.000017;
            } // end if
            double a = polara(x, y);
            double r = polarr(x, y);
            a = a + ap;
            if (a == 0)
            {
                a = 0.000017;
            } // end if
            x = r * Math.cos(a);
            y = r * Math.sin(a);
            double d = x;
            x = y;
            y = 0;
            if (y == 0)
            {
                y = 0.000017;
            } // end if
            a = polara(x, y);
            r = polarr(x, y);
            a = a + i_n;
            if (a == 0)
            {
                a = 0.000017;
            } // end if
            x = r * Math.cos(a);
            y = r * Math.sin(a);
            double g = y;
            y = x;
            x = d;
            if (y == 0)
            {
                y = 0.000017;
            } // end if
            a = polara(x, y);
            r = polarr(x, y);
            a = a + an;
            if (a < 0)
            {
                a = a + 6.283185;
            } // end if
            if (a == 0)
            {
                a = 0.000017;
            } // end if
            x = r * Math.cos(a);
            y = r * Math.sin(a);
            double xh = x;
            double yh = y;
            double zh = g;
            if (i == 0)
            {
                xa = -xh;
                ya = -yh;
                za = -zh;
                ab = 0;
            }
            else
            {
                xw = xh + xa;
                yw = yh + ya;
                zw = zh + za;
            } // end else if
            x = au * (Math.cos(ea) - e);
            y = au * Math.sin(ea) * Math.pow(Math.abs(1 - e * e), 0.500000);
            if (y == 0)
            {
                y = 0.000017;
            } // end if
            a = polara(x, y);
            r = polarr(x, y);
            a = a + ap;
            if (a == 0)
            {
                a = 0.000017;
            } // end if
            x = r * Math.cos(a);
            y = r * Math.sin(a);
            d = x;
            x = y;
            y = 0;
            if (y == 0)
            {
                y = 0.000017;
            } // end if
            a = polara(x, y);
            r = polarr(x, y);
            a = a + i_n;
            if (a == 0)
            {
                a = 0.000017;
            } // end if
            x = r * Math.cos(a);
            y = r * Math.sin(a);
            g = y;
            y = x;
            x = d;
            if (y == 0)
            {
                y = 0.000017;
            } // end if
            a = polara(x, y);
            r = polarr(x, y);
            a = a + an;
            if (a < 0)
            {
                a = a + 6.283185;
            } // end if
            if (a == 0)
            {
                a = 0.000017;
            } // end if
            x = r * Math.cos(a);
            y = r * Math.sin(a);
            double xx = x;
            double yy = y;
            double zz = g;
            double xk = (xx * yh - yy * xh) / (xx * xx + yy * yy);
            double br = 0;
            double a2 = a;
            double r2 = r;
            double x2 = xx;
            double y2 = yy;
            double z2 = zz;
            if (y2 == 0)
            {
                y2 = 0.000017;
            } // end if
            a2 = polara(x2, y2);
            r2 = polarr(x2, y2);
            double c = 57.295780 * a2 + br;
            if (i == 0 && ab == 1)
            {
                c = rem(c + 180, 360);
            } // end if
            c = rem(c, 360);
            ss = c;
            y2 = z2;
            x2 = r;
            if (y2 == 0)
            {
                y2 = 0.000017;
            } // end if
            a2 = polara(x2, y2);
            r2 = polarr(x2, y2);
            if (a2 > 0.350000)
            {
                a2 = a2 + 6.283185;
            } // end if
            p = 57.295780 * a2;
            ab = 1;
            if (i == 0)
            {
                x1 = xx;
                y_1 = yy;
                z1 = zz;
            }
            else
            {
                xx = xx - x1;
                yy = yy - y_1;
                zz = zz - z1;
                xk = (xx * yw - yy * xw) / (xx * xx + yy * yy);
            } // end else if
            br = 0.005768 * Math.sqrt(xx * xx + yy * yy + zz * zz) * 180 / 3.141593 * xk;
            a2 = a;
            r2 = r;
            x2 = xx;
            y2 = yy;
            z2 = zz;
            if (y2 == 0)
            {
                y2 = 0.000017;
            } // end if
            a2 = polara(x2, y2);
            r2 = polarr(x2, y2);
            c = 57.295780 * a2 + br;
            if (i == 0 && ab == 1)
            {
                c = rem(c + 180, 360);
            } // end if
            c = rem(c, 360);
//            self.planetDatas[i] = @(c);
            _planetDatas[i] = c;
        }
        System.out.println(Arrays.toString(_planetDatas));
    }

    private void calculateAspects() {
        System.out.println("t:"+t+",gmt:"+gmt);
        double ra = 0.017453 * rem((6.646066 + 2400.051300 * t + 0.000026 * t * t + gmt) * 15 + longitude, 360);
        /*System.out.println((6.646066 + 2400.051300 * t + 0.000026 * t * t + gmt)+"");
        System.out.println(rem((6.646066 + 2400.051300 * t + 0.000026 * t * t + gmt) * 15 - longitude, 360)+"");*/
        double ob = 0.017453 * (23.452294 - 0.013013 * t);
        double asn = Math.atan(Math.cos(ra) / (-Math.sin(ra) * Math.cos(ob) - Math.tan(0.017453 * latitude) * Math.sin(ob)));
        System.out.println("ra:"+ra+",ob:"+ob+",asn:"+asn);
        if (asn < 0)
        {
            asn = asn + 3.141593;
        } // end if
        if (Math.cos(ra) < 0)
        {
            asn = asn + 3.141593;
        } // end if
        double ast = rem(57.295780 * asn, 360);
        double x = Math.atan(Math.tan(ra) / Math.cos(ob));
        System.out.println("ast:"+ast+",x:"+x);
        if (x < 0)
        {
            x = x + 3.141593;
        } // end if
        if (Math.sin(ra) < 0)
        {
            x = x + 3.141593;
        } // end if
        double mc = rem(57.295780 * x, 360);
        house[0] = ast;
        double r1 = ra + 2.094395;
        house[1] = rem(plac(1.500000, 1, r1, ra, ob, latitude), 360);
        r1 = ra + 2.617994;
        house[2] = rem(plac(3, 1, r1, ra, ob, latitude), 360);
        house[3] = rem(mc + 180, 360);
        r1 = ra + 0.523599;
        house[4] = rem(plac(3, 0, r1, ra, ob, latitude) + 180, 360);
        r1 = ra + 1.047198;
        house[5] = rem(plac(1.500000, 0, r1, ra, ob, latitude) + 180, 360);
        for (int i = 6; i < 12; i++)
        {
            house[i] = rem(house[i - 6] + 180, 360);
        } // end of for
        for (int i = 0; i < 12; i++)
        {
            int flag = 0;
            cusp1 = house[i];
            if (i == 11)
            {
                cusp2 = house[0];
            }
            else
            {
                cusp2 = house[i + 1];
            } // end else if
            if (cusp2 < cusp1)
            {
                cusp1 = rem(cusp1 + 180, 360);
                cusp2 = rem(cusp2 + 180, 360);
                flag = 1;
            } // end if
            for (int j = 0; j < 10; j++)
            {
                if (flag == 0)
                {
                    temp = _planetDatas[j];
                }
                else
                {
                    temp = rem(_planetDatas[j] + 180, 360);

                } // end else if
                if (temp >= cusp1 && temp < cusp2)
                {
                    inhouse[j] = i;
                } // end if
            } // end of for
        } // end of for
        System.out.println(Arrays.toString(house));
        _aspects = new ArrayList<>();
        List<HorosNormalEntity> PlanetResData = HORFunction.getResData(HORFunction.PLANET_DATA);
        List<HorosNormalEntity> AspectData = HORFunction.getResData(HORFunction.ASPECT_TYPE_DATA);
        k = 0;
        for (int i = 0; i < 10; i++)
        {
            for (int j = i + 1; j < 10; j++)
            {
                span = Math.abs(_planetDatas[i] - _planetDatas[j]);
                if (span > 180)
                {
                    span = 360 - span;
                } // end if
                if (Math.abs(span) < 7)
                {
                    HORAspect pl = new HORAspect();
                    pl.set_planet1(PlanetResData.get(i));
                    pl.set_planet2(PlanetResData.get(j));
                    pl.setType(AspectData.get(0));
                    pl.setOrb(Math.abs(span));
                    _aspects.add(pl);
                } // end if
                if (Math.abs(span - 180) < 6)
                {
                    HORAspect pl = new HORAspect();
                    pl.set_planet1(PlanetResData.get(i));
                    pl.set_planet2(PlanetResData.get(j));
                    pl.setType(AspectData.get(1));
                    pl.setOrb(Math.abs(span - 180));
                    _aspects.add(pl);
                } // end if
                if (Math.abs(span - 90) < 6)
                {
                    HORAspect pl = new HORAspect();
                    pl.set_planet1(PlanetResData.get(i));
                    pl.set_planet2(PlanetResData.get(j));
                    pl.setType(AspectData.get(2));
                    pl.setOrb(Math.abs(span - 90));
                    _aspects.add(pl);
                } // end if
                if (Math.abs(span - 120) < 6)
                {
                    HORAspect pl = new HORAspect();
                    pl.set_planet1(PlanetResData.get(i));
                    pl.set_planet2(PlanetResData.get(j));
                    pl.setType(AspectData.get(3));
                    pl.setOrb(Math.abs(span - 120));
                    _aspects.add(pl);
                } // end if
                if (Math.abs(span - 60) < 5)
                {
                    HORAspect pl = new HORAspect();
                    pl.set_planet1(PlanetResData.get(i));
                    pl.set_planet2(PlanetResData.get(j));
                    pl.setType(AspectData.get(4));
                    pl.setOrb(Math.abs(span - 60));
                    _aspects.add(pl);
                }
            }
        }
    }

    private double degree(double l) {
        double h = rem(l, 30);
        return h;
    }

    private List<HORHouseData> calculateHouseDatas()
    {
        List<HORHouseData> list = new ArrayList<>();
        List<HorosNormalEntity> horos = HORFunction.getResData(HORFunction.HOROS_DATA);
        List<HorosNormalEntity> planets = HORFunction.getResData(HORFunction.PLANET_DATA);
        for (int i = 0; i < 12; i++)
        {
            HORHouseData houseData = new HORHouseData();
            houseData.setIndex(i);

            double l = house[i];
            int z = (int)(l - rem(l, 30)) / 30;
            houseData.setSign(horos.get(z));
            houseData.setOffsetAngle(degree(l));
            List<HorosNormalEntity> dataPlanents = new ArrayList<>();
            for (int j = 0; j < 10; j++)
            {
                if (inhouse[j] == i)
                {
                    dataPlanents.add(planets.get(j));
                }
                houseData.setPlanets(dataPlanents);
            }
            list.add(houseData);
        }
        /*Collections.sort(list, new Comparator<HORHouseData>() {
            @Override
            public int compare(HORHouseData o1, HORHouseData o2) {
                return o1.getSign().getId() - o2.getSign().getId();
            }
        });*/
        return list;
    }

    private List<HORPlanetData> calculatePlanetDatas()
    {
        List<HORPlanetData> list = new ArrayList<>();
        List<HorosNormalEntity> planets = HORFunction.getResData(HORFunction.PLANET_DATA);
        List<HorosNormalEntity> horos = HORFunction.getResData(HORFunction.HOROS_DATA);
        for (int i = 0; i < 10; i++)
        {
            HORPlanetData  planetData = new HORPlanetData();
            planetData.setPlanet(planets.get(i));
            planetData.setPlanetIdx(planets.get(i).getId());
            double l = _planetDatas[i];
            int z = (int)(l - rem(l, 30)) / 30;
            if (z < horos.size()) {
                planetData.setSign(horos.get(z));
                planetData.setOffsetAngle(degree(l));
                planetData.setHouse(inhouse[i]);
                list.add(planetData);
            }
        }
        return list;
    }

    public static List<HORAspect> calculatorSynastryAspectDatasWithMainPlanetDatas(List<HORPlanetData> mainPlanetDatas, List<HORPlanetData> subPlanetDatas){

        List<HORAspect> list = new ArrayList<>();
        List<HorosNormalEntity> _aspectType = HORFunction.getResData(HORFunction.ASPECT_TYPE_DATA);
        for (int i = 0; i < 10; i++)
        {
            for (int j = i + 1; j < 10; j++)
            {
                double offset = 0;
                HORPlanetData  mainPlanetData = mainPlanetDatas.get(i);
                HORPlanetData  subPlanetData = subPlanetDatas.get(j);

                double mainAngle = 360 - (30 * mainPlanetData.getSign().getId() + mainPlanetData.getOffsetAngle());
                double subAngle = 360 - (30 * subPlanetData.getSign().getId() + subPlanetData.getOffsetAngle());

                offset = Math.abs(mainAngle - subAngle);
                if (offset > 180)
                {
                    offset = 360 - offset;
                }
                if (Math.abs(offset) < 7)
                {
                    HORAspect  pl = new HORAspect();
                    pl.set_planet1(mainPlanetDatas.get(i).getPlanet());
                    pl.set_planet2(subPlanetDatas.get(j).getPlanet());
                    pl.setType(_aspectType.get(0));
                    pl.setOrb(Math.abs(offset));
                    list.add(pl);
                }
                else if (Math.abs(offset - 180) < 6)
                {
                    HORAspect  pl = new HORAspect();
                    pl.set_planet1(mainPlanetDatas.get(i).getPlanet());
                    pl.set_planet2(subPlanetDatas.get(j).getPlanet());
                    pl.setType(_aspectType.get(1));
                    pl.setOrb(Math.abs(offset - 180));
                    list.add(pl);
                }
                else if (Math.abs(offset - 90) < 6)
                {
                    HORAspect  pl = new HORAspect();
                    pl.set_planet1(mainPlanetDatas.get(i).getPlanet());
                    pl.set_planet2(subPlanetDatas.get(j).getPlanet());
                    pl.setType(_aspectType.get(2));
                    pl.setOrb(Math.abs(offset - 90));
                    list.add(pl);
                }
                else if (Math.abs(offset - 120) < 6)
                {
                    HORAspect  pl = new HORAspect();
                    pl.set_planet1(mainPlanetDatas.get(i).getPlanet());
                    pl.set_planet2(subPlanetDatas.get(j).getPlanet());
                    pl.setType(_aspectType.get(3));
                    pl.setOrb(Math.abs(offset - 120));
                    list.add(pl);
                }
                else if (Math.abs(offset - 60) < 5)
                {
                    HORAspect  pl = new HORAspect();
                    pl.set_planet1(mainPlanetDatas.get(i).getPlanet());
                    pl.set_planet2(subPlanetDatas.get(j).getPlanet());
                    pl.setType(_aspectType.get(4));
                    pl.setOrb(Math.abs(offset - 60));
                    list.add(pl);
                }
            }
        }
        return list;
    }

    public void calculateSynastryPlanetHouseDate(List<HORPlanetData> horPlanetDatas, List<HORHouseData> mainHouseDates) {
        for (HORPlanetData  planetData : horPlanetDatas) {
            double planetAngle = planetData.absAngle();

            for (HORHouseData  houseData : mainHouseDates) {
                double fromAngle = houseData.startAbsAngle();
                int nextHouse = houseData.getIndex() + 1;
                if (nextHouse == 12) {
                    nextHouse = 0;
                }
                HORHouseData   nextHouseDate = mainHouseDates.get(nextHouse);
                double toAngle = nextHouseDate.startAbsAngle();
                while (toAngle < fromAngle) {
                    toAngle += 360;
                }
                while (planetAngle < fromAngle) {
                    planetAngle += 360;
                }
                if (planetAngle > fromAngle && planetAngle < toAngle) {
                    planetData.setSynastryHouse(houseData.getIndex());
                    break;
                }
            }
        }
    }

    /*minute = 14,
	hour = 3,
	day = 14,
	timezone = 12,
	month = 11,
	year = 1985,
	latitude = 22.571621,
	longtitude = 114.003462,
	daylight = 0*/
    /*public HORUtils(int type){
        //设置用户数据
        year = 1985;
        month = 11;
        day = 14;
        hour = 3;
        minute = 14;
        day_light = 0;
        timezone = 6;
        latitude = 30.50;
        longitude = -87.12;
        //初始化
        init();
    }

    public static void main(String[] args){
        System.out.println(new HORUtils(0).calculateWithBornInfo().getHouseDatas().toString());
    }*/


}
