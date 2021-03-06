(function(root, factory) {
	if (typeof define === 'function' && define.amd) {
		define(['exports', 'echarts'], factory);
	} else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
		factory(exports, require('echarts'));
	} else {
		factory({}, root.echarts);
	}
}(this, function(exports, echarts) {
	var log = function(msg) {
		if (typeof console !== 'undefined') {
			console && console.error && console.error(msg);
		}
	};
	
	if (!echarts) {
		log('ECharts is not Loaded');
		return;
	}
	if (!echarts.registerMap) {
		log('ECharts Map is not loaded');
		return;
	}
	echarts.registerMap('countries/uy/uy-all', {
		"type": "FeatureCollection",
		"features": [{
			"type": "Feature",
			"properties": {
				"name": "Artigas",
				"iso-code":"UY-AR",
				"center":[-57.0370,-30.6180]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@A@}vuZYvÁtBzKlKZ·~oQY@oc«CÝ]»XgIZéG]QIoaĕ_riP}B}rNĽäë]aN¯ÂVÓAJqQQLWxc`SQ^xIºPfvrºvvxÈ~°^nRZ¨XZ~AcXmYkfm¤_VjrF\\SCEkpI^Yrh{òFRlx`A¨rv|¶fA\\jWjd~F_pNt[jjCcpgGq¸ompAVkxwf~U@ofA|x\\p¡rA^[KolwKhsUZi¦eV}xHf¡dwLsufsrJDmUf­^geEíOÃT"],
				"encodeOffsets": [
					[-57353, -31828]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Canelones",
				"iso-code":"UY-CA",
				"center":[-55.9909,-34.5465]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@P¶ªjIxe|PTaHbUnl`n¤N¤n´S|_^|YxDvnÐITrMX¬ůR@£K¡_uVwqVT¿G}mQËxBSO÷MBÍr­MdÅg}VUå{ıÇTFtQd§_SvgJO_F{]CqdSrsjgH{qqnÍ^¢V]zG^gtC`x"],
				"encodeOffsets": [
					[-57745, -35252]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Cerro Largo",
				"iso-code":"UY-CL",
				"center":[-54.2725,-32.3655]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@hf@dtSPYtBfWD¤QhjdE`RXztDrhdP^|b°pnaZGhLzs~NxRKX`rSx|piltdAp¢XJLwĖt¢ʒǹtu¼|thPtaUQaºujmYCspèghcckvLvgNj}r¿U|Y@eveJaxk_P^h}dQbdbkhN²Z`hEXqª_ONqyKeK_OSUNuszwRFS£H_¥fwDgDuSyT§HixïW_kDsc_wHmWñ¨³JsdEj§G[A__s\\s}kbqMK]aM]Ã¬kgoSÅ@«½ÓO_sxuIoPyoçGWvkFlE^yrF¶IxydFvGJcjNpD¼dbf[JhSr"],
				"encodeOffsets": [
					[-56638, -33268]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Colonia",
				"iso-code":"UY-CO",
				"center":[-57.6700,-34.1634]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@lBn]BLb²@Ljjà@`jpXW®zklle^JXºQa¢y~GŒlÄdpIZRbÚ\\v_zÄ@^RaSVU¦EOsIohu[PobMG¯]SY]Nb£aQHw[s³RÝDM}ZF£]_^yI¥m|GÁc_aL_ÅEQtXWZC ¹Ú«¦©hChaxµhÑFYlNhc¤­ ¼¯pxSÚ"],
				"encodeOffsets": [
					[-59834, -34653]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Durazno",
				"iso-code":"UY-DU",
				"center":[-56.0031,-33.0218]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@~cYtWnxp|CWydvGXf]GyxKK~HÂ@ªf~nDpbNjUÊf¾WÊLnpxLr\\REzcKdN@|nOdB~QMtLAohU@\\¦FetLYD¶fFDjZÎsTqIg\\aecC»ModiIuHEzcJwEµzqF]klEXuèHzppOJ[ik½csGy©]IPS_eIca[Gc}YULµODUqUAags·PirkowUUm{W}Dg»pYWB¯wSÛOÁvmqkCilT`u`õCmVOloh_s\\uHW{¯KwnqOy`WNX[x\\KiXBlqFIz]@j§JIPXlmtSp}Kud¡KċdafCjqT@iZQWJj@jNzeÐm^TCvZ@ZRjxh[\\uzPirtcVTmpVf `c¨VdVL"],
				"encodeOffsets": [
					[-58043, -33582]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Flores",
				"iso-code":"UY-FS",
				"center":[-56.8807,-33.5816]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@^BrSDibeČc¢Lvc~LTonsWkOJ¨Ii^@JyrEAkjWL[\\wMWXz_jaQ{Z}GY`qlë\\mRIK¿m]y¹aIçvsyWÑ{ȵbcWPtQ_hNdQsfeI|X\\PZOZXhYfwAYx}j[DfyNU²¹MZjbAdXFzZ\\b¬`L"],
				"encodeOffsets": [
					[-58513, -33914]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Florida",
				"iso-code":"UY-FD",
				"center":[-55.9919,-33.8352]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@rPxm°LX|G[v`tpgPknUöDv_S_jklDnrÂuÜPT°xAZX¼oh~C|XVnxVlpjq¸OhtBbVrCV¶PVK~ZHd\\dbJ`fTO^JzªHEgdeSXSmOmw@{aT¡jgo}R[OuwZKWaS¡]e§IcmyjµS_kf[£yceÃ{co¯k}]e[NgIsaWD{m_eaWqNSJÏumwC{Z]{`³T£mMm£k_maVGSb{OwfJ©iOµjIpqPEh\\hjCLvSdlKrJ~`|DtaPDlSBmA]pZZQtz^nLÀJQ[nkì_rHZY~R|ib"],
				"encodeOffsets": [
					[-57893, -34296]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Lavalleja",
				"iso-code":"UY-LA",
				"center":[-55.0377,-33.9694]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@fb`nC|bXJth\\Mf^l~p°|dfÄzd\\¤e`lTi¶nzd¨J^fT¢XbLxYPvQ\\p~`hbCGJ|ÐZr]B~O¬`¬rgÌµXm~ENªSTUA`YpRB¸WjjJGkPa®ÞYWmÝMU]Q{MAeW@WWU«X­S}{H}U­]emCOyCwYUkBs_Uq·]uTeGwqDq_mgIgmiLqQ_DWogRUYo_½aLsgLwPM]Ë¡ajSw_Q_Ui{MYaHāBQyPSÀUrUx`vL¢¤Q@«Ű"],
				"encodeOffsets": [
					[-56959, -35028]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Maldonado",
				"iso-code":"UY-MA",
				"center":[-54.8850,-34.6185]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@zORĂAGZb|NjVR`x`TibÌ¢N^OxKthbK`¾ZpVhQXpCR`KrnjJhnh`CrxrfHvS¸^Vrt`AVlxZzDFYtuQsV[aFkQ·NçIçm[hqC_uBynmH¥\\Ch»@¡NgGXoYB}e]§o]WË[e½µ[ÛXADf^ÍCÏ[iU^Jf¤y`JcVTPwARÌnH~"],
				"encodeOffsets": [
					[-56811, -35458]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Montevideo",
				"iso-code":"UY-MO",
				"center":[-56.2017,-34.8216]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@\\|Wx|rhGtiTqcrD|^`EPhITu¨`RcsESm¯B}oZgF@eÓEen{TsjPtCN`"],
				"encodeOffsets": [
					[-57708, -35618]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Paysandú",
				"iso-code":"UY-PA",
				"center":[-57.3465,-32.0677]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@\\nFdi`ÈRF_Dp]UVwvkT}F`WVX_BSÂÜy¶KpiK¶qAri°B\\K¶EjNC iAl^Z¼Ots|@iÊQpR|käSrQ^rLvIj¨rRzENgQ¿±OmBma{RuUYarucqK}cKoqy{oQEieI³C{c©@gfuAgTIb{dcQÉHaSfSLmTOaVgÿ]uDQ}kacgAWtwN{OgNilYOqU_gP¿mVaQNG­ÛgDÝbHjZorC\\o|LOgQtnôZ\\jh~LnQÉÐQl^´rLCvU`e¬qfYffÀjT ¾JjzJrxÖo¶{h"],
				"encodeOffsets": [
					[-59426, -32265]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Río Negro",
				"iso-code":"UY-RN",
				"center":[-57.5267,-32.7385]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@VxB°¦NeH¸²®fÀJÔp`rxEwAOxH[c´L¸ISC~uOhPKp{D[pqYGiÞaChÜ®HRMbnUÀhO` VPrZk jhM|PxMXshBbdlR~vCĀ^hbUPnSKTebTÊGdR|cJahSvBe@[cK}]S[¹¥¯Z}AsUKc§U_dUenoUSsdjqyO[vg\\iwQ@YYDu]SÏnyfM@iiIXRjY@]AsLa[[ip}]M`ytd­PWsiAiO~¥LcdqcËmQuDkd¥NSZwD[{rXUmq[Cum\\U@k[kT¹VMenf»A¯^yMH`weÁhs_qÛUoU"],
				"encodeOffsets": [
					[-59845, -34243]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Rivera",
				"iso-code":"UY-RV",
				"center":[-55.2331,-31.4892]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@XN`ØlApf~vr`B@~R¤Y|f~C\\ptDfzA¢Vp²HFj^TGTjPdMvT§WLmrynB}Um~]GngSisUhW|@BibOlHZM¦Ky]qrX|Bht[EutKKmCuGStqXL¸[v_ìEVq¼{¹N`wIWs¡xĕKWI¡Bocksj{oTwqW_LwQûĞ_Tº°kDivEbNZaA}j³F]oVáF«de`ÙEU¡@ãXioTF{thúkHTylRraXqSoL]neBMf\\ÀwEåS`sNwndt¡ZgyH"],
				"encodeOffsets": [
					[-57533, -31996]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Rocha",
				"iso-code":"UY-RO",
				"center":[-54.0168,-33.9196]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@PnD^fV®G~|T~W®V¬XX@XBf|NRV^ÞNXn`V®@h~LXbRxfMfBfpZZ¦JQndVIdƠ¬Fy§[gDzOiuSZfrcNaH¥P}QoHøIJ}fqD¥¯¹sGm@YkLigo}āã¡¥YPfWW_IuqKµqŧÝċÅµCmPCÛqƓ¿ġ©f^A~pZWHMh@¢¼g[DG¦mnAz`vrD\\gn JèMèR¸ElbU\\RtsvZE"],
				"encodeOffsets": [
					[-55791, -34743]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Salto",
				"iso-code":"UY-SA",
				"center":[-57.1020,-31.3841]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@RIrBÔU°ÁbMì^ľã M~qA~jO`qĖpbJ^RêH YhJ¼WÞ^¬Dpd@ZRp¸}YLkLyAÂsZuvY~u_uqe}Bo×kM_W~Cu{iNocoO{yqWR[§Wc]»_G[zcAWYw^Q§qJiuqK]qRãT{loQÉRj{@st»PYk]BjDiMµF[L¯AqjBµrLojµLÛzÁTAW`U_XES~ulUxVo^CE`ÇQ_cjE[mrA~fbpel^ÒM®Nxt^ÀUwD~vXInXH~JĖ"],
				"encodeOffsets": [
					[-59188, -31517]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "San José",
				"iso-code":"UY-SJ",
				"center":[-56.7351,-34.2936]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@t\\GxbRa¤MZ^T^°HaNOp\\gvJpdXȶaÒ|XztèubJºRsYY^onBATCkbOCs_{}IqLckTKuiD[gFgrOJoiwD_hs]H^yU¡Î]rmXw[{·\\d¯D±QÃ¡`»XõZg^Zq±«z¨pcF"],
				"encodeOffsets": [
					[-58523, -35273]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Soriano",
				"iso-code":"UY-SO",
				"center":[-57.8410,-33.4922]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@pVÜVr`tÂgf_xGzN°]Be¼mNfºUlSl\\@[VvnDr\\VnW|q\\xCTY¦MlcvCRÌnrddc¦KP}jjBXt®OczsN_~^jo\\b\\tK_Ka«[yYEWBciaNYºV±zMCe\\~iZwxBZeWgPYY[OWJ{fetRMc`gR¥FUVTb]QÃ@yu`Ù[QaYoJÃckő}H¡zb¹RIW]kfkyl­XoW_iß@iKi±@KaAm^kAKèFW~{cÊ\\Q°"],
				"encodeOffsets": [
					[-59845, -34243]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Tacuarembó",
				"iso-code":"UY-TA",
				"center":[-55.7074,-32.0561]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@ tBY~¦°\\º^TL~\\d@@hdªD|´fJFjpR|zprL~dLdrqvZbVQvb|AnPn²RÀMhyF]xXZByd\\`H^¼Xd¨Q\\XzrP|dpMp|jDv}zGh¢YscxmtMT_æFx[¿NefA^mpKrTbWQqzkSlGgù|sEpSjäW¢@VÚFf_¬câEpU^´E~iBbYMFajulC¯¹`Süĝ}MytKgHbYoma¯]{OcqgsCWy_QcFgi£RCeXsAOZTcs@geÍtYiCEeCµZsKf¥E@[gVBpsKNRA}cmP@{cMLydF[QKqwmoÉK½XÉeVMioamC}©eÁ@}GLwLHze^WuHcXz{DwomsXZ}d"],
				"encodeOffsets": [
					[-58043, -33582]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Treinta Y Tres",
				"iso-code":"UY-TT",
				"center":[-54.2808,-33.0588]
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@t¾djl\\wvt`ÔP¬¾Æ@ThplÄ«N^bL^rNlat~t[`B`\\¨HiFtc´Iò§nXG`xtdlCX`ðjw¨GzSvTC hxC¦e`¤GTExQyvtM@`s½ykjiċE»QOyeYAckag^kYVwdwƟ«JccURm¥IYoYAeeeNwQWaKg}­@_UÝZ­OblHiIiX·AoQ_ZBSV©TM}FWnË¶h«q«_}PAq^ÏYI{HaD_gihS¢b@|xPnnWTTcfFh"],
				"encodeOffsets": [
					[-56457, -33898]
				]
			}
		}],
		"UTF8Encoding": true
	});
}));