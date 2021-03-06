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
	echarts.registerMap('countries/mx/mx-all', {
		"type": "FeatureCollection",
		"features": [{
			"type": "Feature",
			"properties": {
				"name": "Aguascalientes",
				"iso-code":"MX-AGU"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@CwYMef]NFIEDŝeSE}[ME[[gmImgaK]YKaNMeNYËG]u_GjEPRcXaD]rW@×YV@pIgSIoeBQUmBqV_iBLU@YZNdjÀn`pjdLxÄJbnM`~|Jp{dDŢAjFJVJR\\Q^JZÞ\\TEhrFB~Rn@`\\FPp[dkEUC@S@GYEE³IdF\\dMP[PnUCÅy@]]x@QqQfSð³bYFY"],
				"encodeOffsets": [
					[-104306, 22558]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Baja California",
				"iso-code":"MX-BCN"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@Si^rO\\­OoTsGaWKc¦Lz]V@[SXxXOf"],
					["@@XbXGNaeIWR"],
					["@@^dlG`STyns¤feKgV{KY\\VwtQA"],
					["@@k@YtaRg"],
					["@@­Ax~\\XZOHqGUyD]"],
					["@@°eo£aSYTUpb[ZkLWhaZ"],
					["@@K@Wd"],
					["@@tKMe"],
					["@@J`Lhb¸Âh|khìjbr]CsTkRGFmTwUhAyCgNaOWBosJWOqH_mfaF]OO"],
					["@@rHnIjT¦_i^[Lc|]jXyXYo{Jp_KNQlFrCfLrGrH^SQ]AoJ_CālE]X_jMlCWwB_Qmt\\Wh{Ve^AV}xIP\\ajfqD^[d©PMTKzg\\Kb¢gRG"],
					["@@CUSfgOCGOP{}N[]OJYCUqX[È­]ěUDßjQLcnQVa\\LfO~An\\MAqUDnV©OgiGSwUKsÝF¥GmUKEqOKD{UĕPsQÅL¥byhc`KjkWKU}J]^[ĂTLIWT@ÑJIUoXIjtH}fQPW­BkKOs`iU[@gcqJ_XUbD_|gNgZ]BkNgVMB_ZOd_XSX{Ob]HYG{ZeQ_\\ULobU^B`qK^TGZVRjW`F\\YnMVkXFcz\\G^[lA¦©\\Km|UBavSROrQXe~o¶XdeOig`yjYJmbwTKgEa`mDkY]³zEdJPjYPFh´PI]\\Wh^Pw\\CIc\\iIhxDhrhxGfaD[bYJC]írcL]Cs`B}TkpYNSOb]nMf@ TVzsNclP[Vkk¡IoDyT]reXk_ET^cUWKuReUȟ@Ǚ@ɡ@Ǚ@ͩ@hjIhm^uBMpBxUdbLKMSM@VgtH[Ar``aZRfKgmgdútÈT¢IVgb@vcPQnbmCWTNzAbQtebRI@|[`cD[j}HFp_pEhabWM]vC|[daCSp~CaFKtmb]S[hDlMW\\If[^c@c\\sAkLKfUB@nUrgViHgXcxoRI`o`¢}jFeZ¥j»HYCoV[AUxgdQBjsLeVNm^O^{tgRcAqQefzizcLaE»¤gZZ¦Baº¢X_AA^Z®VÄgAfNAÆMxºtkNi@VrCZR^}NHoWYm\\CVwAPgjoCk\\_oGBvYYHInWX^ĂLÐAKYdIÀgR~gTYCwjQnenaLmcHaQwCSvX`ìDsKdSJ]gdM`oVCR|C\\[NcnckpzAKyjmVQpkB@b^PlB[T^RzygbeCwzTP¼nPDcphRdLzSLVGl[hqAuTgrsToASRCMd`EgP}M^LtIÄS|W^Oc|oZÃ`edWggUe~GbA¦ʪt̊|Ȋd˜tΤːnȀ`Ȯ`sC[ciVqSUN¡U"]
				],
				"encodeOffsets": [
					[
						[-121166, 29870]
					],
					[
						[-117169, 30687]
					],
					[
						[-117556, 32565]
					],
					[
						[-116814, 28672]
					],
					[
						[-116889, 28673]
					],
					[
						[-115603, 29367]
					],
					[
						[-115643, 29392]
					],
					[
						[-116252, 29794]
					],
					[
						[-118124, 28755]
					],
					[
						[-116318, 30150]
					],
					[
						[-117569, 33274]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Campeche",
				"iso-code":"MX-CAM"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@\\AQ_EV"],
					["@@PZDVZfbZjFcmWOZi_qisiLoWSBil"],
					["@@ÄZÀPŞPL¨DJôNtA`U~qfI]eP[c]NS^LXY`aSk]I^WT[IWNO·Gei{XwBcI[wDcnYvChN\\ZN^Gn^PQScF[hHThpCKuDWc~C`\\g JP~{tE°yB_\\AUDkufSKgbKL]ZE\\mXB\\WdxRQdwPC`fL[°USQ~AL|PFcjZAhjh\\bDb[NddX^DtPPdX^fPVnA~b`gZeHQb]XNIMuLETRfW`RnIf¤¨lbehLfrgN´zuNU_uakAQWy_INM_]F_[KUd]YM]{{w}[m]R²¢ `Xª|bÂt¤tÖBP|n |hÈxÐ¾vCTnRFM\\LMTjJ|O\\YHAvZVxEXPpÒ´flhnXhTzfvYHZq°@zL~U¬VHPK^DdUjJ MXPVabj¢OX^x[VPdBhe\\lÄ`~dÎIXXdObAj^HM`hPPLDbKɟCLWĎHDsRAS£¤QAqtJCz]@TPRvxƔǗ@ECKOARyŲƱK»PaVAATk±Tk¦IE}YD^RWjA¥ĮćŲŃ@ʓVAUřCįJķWEHcDƽ@ĉr@HÃaALûg@KÓ^F@ßµAC½¸AAqe@BFBÉ®@DřƯ@ƍAɿBŽAǓDŻAãEáDūBʏAĥB@ŬŏBNZaLEocHLZyFWIBkXUhh_biNO\\NyEKXHUVjwFW\\@]]JaEReW]gAH[SUDsWYLaKHgINqďÀinCeÁVipkPsGLWv[Gm^yg@oWbDbid@XWN@GR`Z}¸AjR¦KBêōA[TaSS[WDC`qduN[DGZq]`C"]
				],
				"encodeOffsets": [
					[
						[-92574, 21348]
					],
					[
						[-94054, 19100]
					],
					[
						[-94688, 19099]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Chiapas",
				"iso-code":"MX-CHP"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@NvR@bnjRKvP@LPxBC~vH~rþŀPvMjX\\FfdINtJRxr^@T|HIfNyNdnETpLzlZ~@V[X@^\\nGfZPljp}VCpXhMJ[KF[p[dB\\S@UYOm`}\\rPI¨BdaZu]y]E]ZUHiI§JteV_EafQTZAVmd\\¨PxSRjVVKS}IDMrELBSpkGm^_vFl`bBR^@rtZdFfVX`Ext|dMrPTlªJMxV¶MrRZA¢ZT¤S\\UªLQbfhOB]lDKZ~AtbfSTVU\\vXOX|lQt@LSciH]rJFlZPlYjHjObdbOQeAcPiBgQweBi[XUpGb_Mcd@^OEcRgS{f_L[CoLo]]Eq}qdOOc[zIDQ¢_BljEBWhSXD¤qdRhSDwjQXeMikPWICoYyZtW~CbWNWC_^CTj¢E\\WOqHXQpA`q@S\\iz{jWxDPQCabSTa~{Q[\\W¨YxiIzPNlI@e]O[C]\\kJ}F@kYRY¢{@cv]R`_E[PfCTai[@VK`R\\aQcnBNgZB_jOVXUIlAHXc\\RN]S}F]]EgoM@kb_OM_NOgtMoSI_Fmx[DUoQAShGBgoHIg`ǡBʽAǫBʉ@ɻAōȅŃǹēƣqýƁŰƿo«[WHOeWOLWEuJSCyRcUmoSU[GePybuS_@]ZMTaJyGigYwDYGmoYiąøFPmféâıļkrĻłáàANƓƀſŤýä¿¬ŇĒÝ¸ıìï´ÿ®³x}VJVWÄ}MzOTnY^Ghy[DsZqIiBu^cESl_HH[dIv^L T\\HfQNVjc®õŎĀȞĬʌÀž"],
				"encodeOffsets": [
					[-96120, 17549]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Chihuahua",
				"iso-code":"MX-CHH"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@¤Bо@@ɾ@Ɯ̼@̒@Ǌ@ʒ@ɘ@ǰ@NSsfF`ZYeTil]bvLiZkTumPoXYNmhcjKjevYtE|qÔIw^@RYC]bsn@bmlAVG^x@y¼iLabAjYjyH}tnGhe^AXmcVazQ\\UZÎwdCaT[hRUmIaEfUZm XPXQlLL[`SDwZsKtcZ_Dc^m@[XMC]NuESRgAYV[QonahDNYA{^EX]r_Cs\\[hEpoG[pAgOBoRgkJyZIC^mEle@StkHehWIU^]G_\\WVsB`Wbm\\MtmØH_^[V@mvF¤S|kFYjDAdQpRctK\\YZFvyvOBOv]®g\\DG^U\\FìY^_bLPaOijLhU@Ypy\\HÆozHh_LgA]pKhoVMG]rKw²e`x\\EJwdY¥é§T{_Bmy­FE§ûKHµ_~QY]VgpMQaoNo£JiíŽ·IDċǁNEy£õ¤[_£^LdeJX½E]ibQ@{°·_DPwO³\\±^L\\µGğIBYŇ[DkVVNVAõ`]N­[IyÉbBa@Q|ifiretMHgtTbSƋ|@ýACrGWWLŃtFoSlWXi}g]Xs_Wl[Rkico§_¹_IméOANIWÕ×gsWwpMAqutQZv[M}fcH½GyLmyNgsMGUĐƋ·IdeRa_{GobRv]heCmY©ZcKmDAb«HÏŘfËJCHdel]Gqc`K[Qf~§´QQgDtUE³©ccm[mqjQgDNoKkpElWMkUca[mMSM@oNYS[Ac{c\\ñaolFWkTĥƽJeq_Á`]Dc]UCWtAUGS[Q[LYHȃ~l¾zBYcwgM_}UocSoOKYm^¿LÑWµCsÔoDÓ`xND|sdgoRAVc^NŕĀ«zJX­T|LHALVDzk^gfÏ]DM}sYJ[WokfqLO]OcRyATeOgsFQi`yC_^C|ıƌBJ@hXHG{pAXQjwXY@I^x\\xNW\\\\~QXoEVzÉÔ¥EJxF}WchHpQ|bNP^ZB`XBVYzIa`eSigkp]Ecy]dk@SIPyRWEUZkEFem_H]£Ti^TQGvR\\EWUFkneChddlzÆBzIbyLZHºGNµnSnaX]JxYXG|§x_X¿@sTrrJbt^SCq\\YdyPqz_TcØiE`V¦M¨Kp|RJ]dVj]zVZWE¤LXSTWK\\ JÀvMxVCzt@NX®{~bC~jQZfuªVzaLNpRTC^_AQBbEl_hGBØVm^C|jLKXwEoKjiRRªsŐXBDbgRWlgnaDQv^BHb]jX¢CZĐG~UvpBJbLfXoPYp\\kvfHGSlVu]rÊB@VT\\^MÇBkTKtz^]NJG¢QnXXRÀOzFQǒYVWnORdqr[|FF{hR|«JIªYTGtSCtB\\{DKQË\\c\\yTA\\Vªg|j{EZJòQ ŘGAĎĶDAxË@dl]^eJCzÁnWTT~pDYraIZvpÀB¼þ"],
				"encodeOffsets": [
					[-111366, 32085]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Coahuila",
				"iso-code":"MX-COA"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@Mba NKhQlX`BC`hvQXNnp^GXPZRVjTDHlXZNn`FhQld\\HVjXUz^Ed_Jg`Fb``G`PXN^GpP\\O^PX^NnAz|jJ~XL@flAAbVlxTJIlTPCb\\hOnFPTtFvf^olEfzD^UB~WfLIVZf^C\\rrfRlAoM~UZXzU~FXQLFXhMd`CXcfH¼Kj\\d]hh^^@^RabHX[hRVUdV`ahblFpI`rTLWGFnhjNTVC|hVS_]`MLwjW@iXy`X~EZOBgURqZEX\\Cx\\``GR¯aLcfHGhKH[lsfHZSrF_DaZSNqheXk²cT]ijKPunIn]clPsSU`a\\ER_vgvQZ[Rm@ud»K¼Ó@Sf_IYU\\IUHifORGsT{lMb[`B@^WWBYYol®_Q]oUvsJgYuJ[vFWvKvl_p@LOL`Em^BpYOjVSHigVbu|SzCGTKTcdKN[ScB_jaLWBrENo÷ăÅ·cbƟƖĉËÃÇʓɕ¹ſŝHķ`E`s^\\nHlkJPU^CdpG`l^VdEFijUĪyHmISByKMNM]NgCqUmLcH£Um[§ÛÓ¿ĸŵŁƣŝŅċlqäë\\abx{_v]«vUXÉA©|SCXy]«SK]ÉhwD_`qN¨GT~´Jli`M¶YRcÏLe]UIopM~AP{lAWd[eÏrK¨GÌZid]TDXgYxeJcULiRKÛn^¯^D¿`ĭeāÑeƙ~mÄ±ĉéCW~ÕOŻJSIH­z³Ĺ¤¶]ªm[TiFÝp{X¹qUa£}N£Dç\\eUP±tyiõµĆcț¬ſ¢µNƗSbĭȇÊƷʛEwVY`y^qA{hsK}dkAbƋŚ\\fA\\wrNÅ[^[AainMhtKfonoL[XHD^d`dª°nvO^D^i\\AGwNmfO`zµ¶tV\\VDddNTdLA^tZhlVZP\\P°wGʂöŤ±ÞµȬəúME]|LaTSsGhsNqfej{j@RbaAÊJz\\M®^_BöUUMUCl\\ZňJAHĠ[¶]K[²P´Ox`C¯¸|@aR^jFW¾fIKc¤]`£\\öz¤MFČǂJC¸îžIjp¤pMRboNh^URZ`}¶LG¨üEF®nz`AS|¨¦ê"],
				"encodeOffsets": [
					[-105787, 29700]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Colima",
				"iso-code":"MX-COL"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@Jh^L\\IHTSc_WJUSN"],
					["@@TREtPVvl\\rmfKrJeMQAWC^}bwFDPmjcG"],
					["@@K_yKCSekU@dgslmO]N{ccWG]YAkea@SWb­¤Õ¦Ñij·~÷¦÷ûNL~|TI`cjd[JqGIWgBucMlaFiZéluFíxO@hTYZJFklMQ|pBRhE^htzj`aZVJgdKZtlR@`dBQZVbUbfvSjpXK^lS@bXh@NU^dzlNnZh_NT^dnYYDTgÄiQhdTXe`FRapRV{zbrP`TdAfZtvVXCXFZUbqRolSBQmg@±QsbsBWWeCoYaNcQi¥OaL_D}"]
				],
				"encodeOffsets": [
					[
						[-117514, 18787]
					],
					[
						[-113720, 19232]
					],
					[
						[-105972, 19421]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Durango",
				"iso-code":"MX-DUR"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@Ô_pCtÓ¶DÒXÀKn]LZpPTpd~VN`xhdAZ½y}kGȄZ\\KRT\\HVsBDX^VCd_^Âr`IfĦƾlSXkEpb[ò|dBdT\\MZ@pTNN\\ndblVNkXoFLlMphCRri\\nnddª´VFshCRR¨³}e\\RLd_r^HfkGcDIeÌÐŗ¬GBanCdLªYnZfD^gQupa|Hb`fQJcƌ¸ďHVtNhzMnzK¾HdG~e\\NuRYvsrNBxotXhÖØJXMPBêJn`º`p¨jdQlk\\`XWth^j~XWTkpEńsXKXHDqþB@ƌ{K^{NFɚù¶ȫ²ÝõţHʁxO¯[YOUgksYB]cKScMCc[UsU¶µyP_nexMH[B]j]CuP¯mc©c_C]WG\\pKpmLesNgjmb\\B\\]ÆMxqB[[eƌřalB~ctL|grBz]Z_xUFƸʜdmp]{}@@ĜB`ĵdą^ĹMN_G[aÕHO[}McSM¡B~oaeG_Z]xQÇ|aq¹Y\\Uve@ONaUg@kMUfgVWYaIqJoK³IORcIc_HWsukECcmYHiIA_Z{S}Nci_uw_]OqeÅ«¥Ee}JMbg^GWµmFWd[Bq`aGa[M{RqZuSmÈEQcavyUIOYkGgoýµ_KmYiWwEMkQĕ­AũPXKF[`MpwLSQX_KSTcT}{YoJ_OoASZcR]UOaeI_UDi[[Hc]kA]jYMi`iUKcycEasF]buNeEW_YIeqYIOmXóULogMW^aFo|_DepWxD^«DDE[p_M§@qXeHxEZsLMUkFOTcEIćďwMgòÅÒHlXTƐ¦mVTª[j[NOWcCgZShAaZGdZ~cv½eóKùFCu[Y@pcrCrLbi\\kToeHkEVyUKYeKURBu\\kl`¸KXJlcrJbcPqBQ`AjOTSWPp {VQ^^b@^wrQQNGh|IjOQK}MQ_cĄVT\\cPHTYMEm¤PÌÌ{dGRvmeVH|eJK\\^Oth_\\Bts]uAI¤]`¿®eIqf×n·[e_MySYÇgM·dK\\«ÐFVernRwhPDrmHÎItaSNũŪMLJa]UNahPM\\aPmlwfcLÄU¿pLŒq\\B\\fh£ÌIúthvJPX\\Q`eCUVBvLfB^S^VÜ¼k"],
				"encodeOffsets": [
					[-109562, 26278]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Federal District",
				"iso-code":"MX-CMX"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@IHN]SawÅ\\SxûN©Q`_GnO`wXFxsT^fMVppNpfdZE|||^\\bVQPVLH¢YK`Dx`HTsm{`qdB[GSlSgCkcz_ESSaQ«TGCczQw[@g^k]GZ]WKRkO[U"],
				"encodeOffsets": [
					[-101354, 19533]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Guanajuato",
				"iso-code":"MX-GUA"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@\\VndªE`NPGxkRNpFOE[~qzitGÞXÞWbelCLYtMQYfeSdBdQo_PUbF]`CdyErmhFF`jUM[N[rVh\\rFZTÎBRZ_AbXhA\\T|`^bMLrX|S¦JtmrBze^HX]MsNSjbnqXK{\\dFCDQi^UMW^k[tJdaz`E\\\\HLwOyAqP[M[HYecVtwrREX|D[wFPOU{iAQ«Xa@U^TMfLqy^Oi¹UmLMGgLeXOEeW]^@oY_GaR_WOb[yGsSc^Å@poVCDB_q»BeGX@peJDk]KmJa³oaUCUhGK{`sAYM[W^sA_jKVlJXYlSX@ugBI£RKXw@VSDwCU`uDsN_fWDSDcZW_O]i_L_WONa_GWPsaMPK³G£qC[cEFvsOcPXv@UQ]uFMJg[SyyF[SFmhOKDcIgeAK^mIg`{GaN@{aI[VKdeR^\\Hd``k^OAahlhqjuLE][[ipWHcnYm[eAAeeHcj]HwO_VsKuAuR[S[HPfD~YEGdHF\\DOTO`AqvcUkLH¢QVcBY_VYPiaMVkgiR]sRoeAocSQua\\mJwQudm]eTMVo[[ASWaLi^kPLnULFrQhLcFK\\yBcWUBNlGVMTbp~TD@zb\\h~AhvS`pRvJ]xwRa@IlJuLLZuBYZNf{^h|EbLTjRXAvp@ZbCLXfIDtzNRrf\\FtDhTYZEdnJNrbSNLJBvPhC^RVpRNXTpMhZExFzYfoh\\XÌbfEE|Od©¾^Pp¸b^DpSDXrCX^jAbZzkDEnNlEzn"],
				"encodeOffsets": [
					[-103946, 22323]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Guerrero",
				"iso-code":"MX-GRO"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@`WZcbO|Kr]jKXsbGX[n@F[Qac±eG_DYZinH^SN]cB_XaJcfT`h\\EjKkYgI{tE\\oZPtHAjXzlGflTz`V^@P\\^P^ClJlvVZVtoT[v_FZltnLltOv^`ALZ`QV\\Ah\\r`VC`^ZL\\~G\\]jO_~adPt_Zw\\g^FD^KC[ZcSVZxZbdPupmLw`Kd}VIbkA]ROvX@{r@´QDL@AQdYP\\j`tGAUX[ESfSR_VApSmTWLK_bkdZbQM[xWBsMmAÚm~£e¹¸LOhWjIisJeYXO[Z@a^e\\Py`xtbEHLfpB\\I@\\MDkRYC[[a`C[@u¥oaEWTwCWF^ET¡VFdUjkHZmTETaHkVM@oVGP}aDMm~¿dÅcAYsKul{\\OXXEFY[eQFkpUIHQMfWfaSackJea§Xn¡QeOYMI_[]uCqyhSiCuZ@J\\ijFU}qUiaKBXFU]¯WFeR]G_fF^QKgTy£Á±LÇue_YyaT@bïì¬HS_iPaAã|ěqJXËfġvęj¯LÕHIgD»hÑµaLO\\F@j``UVgJcMicE\\ar_RMZw^`½fsJ¥jÅpbţ¦ÉjĥónëďXÓzgDiZ}TPbM`¡}jlKduh·z£h{RëebBxW`gXGQjeUKReEKnZIgeh¹C`opB\\[jkfq`sVħ¸Ñjs@MWaqs@|jMRG|LTKrT¬CZTTLzTHVfnA|R@dZhMLNthIrHn`CZTbRKn\\P[bRrYZB`_\\DZ\\tXPqElTj\\^lÔ| RXEPg`AfPJ^EbkXZshUbEXUdAVQ¤VXO^XfARZTzRN^TSvDtefFvU®GXPZrCFS^eMY`KTP^UTHFprK^vl_fEPRQ^pY^BTk|ZVQA_\\UAWfUdA\\`dHj~C^YJ¹mtYEiBjZª[DfmXZq\\J`Ir^^QrdDZ_ZEjqCyZHljL\\^L\\x\\HV`"],
				"encodeOffsets": [
					[-103027, 19341]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Hidalgo",
				"iso-code":"MX-HID"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@ɴrDp\\DV^FxppHlUJV|^J^bR`AVXzHEv}vWbJfhPvxKNd|DlxTNRbhHC^NddbDZO^VdRHpQraCUnVTZKbL\\ETzzdta@QtaBTZlHR`^ÐBfrQkVHZfVG|XE~`B^ZYVNhIpehQL]vvU\\HzI|LVQdlh]`EfPMrRj]AoZWdRNlCRhQ`^hAdZWXbrPZmCe_KUi»nkhJ~E@_YGzeNbO\\crJE`HHXn^dGhTBecu~M`WnjfMFab_wKcHeI[aYeLWcB][ILgaU[ZgoMUsAqRqvM}@_`ca[Xr\\YdsRBZY^cU_LU[[Ky]RaiaKgcHWk]\\sXfxdVnffBQcsaAeMUwmAOdeISN]WGScoguMU[`WP]fcFW@NiG]Z]zUTFM¤nAdZbHJxxnx`BVhbPBn¬tdCR`xfKFqUYrÃhOQ]@c]CKo³{MwqmUu[cQaPDaGLIPiKSucQF{VevF^dlHV^l@zyMSxCiUWOGkYHMc[QYSdsUq\\giea@kPUck]IiWDAicQw\\Q\\HhKDe^WB]^ifB\\ULseAmgcEcLyCSJiKC[VQA]µÛRcG­Je_c@_WYef[EUcec{EQReCMtDlvGpXaR\\xQrz^N~]PK\\kZIdad[EEgihVdWR¿`gRYUCa£vFT]XSUcRyiNcE[gMm·|PJi°rZGE\\H^^C`hjBFtIxYYAwM]D{NaoAMPFcODWiUH|]HUboKYFIE]_ggOe}UI`[YNUWaYBK_oAOeeIq[DOaqA_QIjLRpeRmS_p]VjV¦\\XnWUpkbEf dJUnaWmNkKuJ}oWf[AId][uJSKeLmbC^©a["],
				"encodeOffsets": [
					[-102255, 20755]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Jalisco",
				"iso-code":"MX-JAL"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@bbpHl|pQVrQHLxZnttrhXT^sv\\bAlW\\j°fA^PArdKRNWjFxOGkkXA@avWbDTSbAlXjGxgA[fWFW`[FefSLSgNJwjlUJSvQjqbKAyNYAaNWpSIfR]hV~I^[Tm¤]Yx]tzYDJjpX`OzVX`UplAjn@RsbKp^`a^NhIVnLWXE`bXJQVNCbVdUÄf@hzZGZ]zMXXLlQlTKntlPDTvbPoX\\fCXTibUrDTeNUbkUqLUdoXJr}Lcji``xGXeZTxXHfvUdsfP~uVKURmDZQJebWFyTXafGhxNg^gE_^Id[F}sSFkO[FVQfNrCjSxRXR¬hlGhXTvMH@HH\\jTT]leZFYLhgFQVZT_nZfXCnTZU~AOZd`pDVWCg`YHoU]AgSWO{D]a@Ce|Hh^KRhEf^Dbn^@DdhXP^Utbidld_VTPBh{J_f]RYKQbkJb`H^`H¸CpJXmnRlMHDKDxUFqWcJeS_L_W_aKM{[IUL]YIQmZiMwWBAkLYUQlaG]YBs[[I_VmHsKi`w^S\\£XMd¨A`zaBXtlRZJÆnAf`¦rDf`^Q¤a``MEWNeZAKoVGBkRO@yVccY_GDc]§yJSsNmf[hGi}UQS{OOW}hgpQhLpOrxnVn_hR@g^GrPNOZRbHdpED^lXrNtY^f]PHlVdSPiBUg[KBWXC^^jT]bXFVvANYdC`\\^K`]fKDysqaEB[UeLe`LP^¨IAb_A[xkCe]YgJYOqeMkgYkHyQCo[UJcQBJyAqRgI[dogOUycMw{As]QRcaYuYWMcYOV{_LYGOYzcXDhAyOCQ[S]JKgXKoUS[DO`_FeeuKTU]SyELeTUxCHixi\\\\XW«eqGUU_Ce\\yOEO_UGkNccAoLavdrJRQlBKoJgGc[[z_\\[EiVIZslUZDXdFTc`AfKMWxwCW^GXZpDRPbOfLFjtIp`hON]ZBeªjT`dNR`Wz\\¦ZbÊPX_zKDiQP|tDJfjjFdXTlQCrVTYfLCb|dIZHd`OZeLibBvDfWhCdfWbrvcd@b±\\FpgCUfYEuTUtQWbbFd\\`xnARVfAJphTJo@ZUØX@^qbCdWQFOi`H^vÌHZMNfbMZLL^hbnnJ\\hF\\\\NF~fTŞ CFJMEe^NfxZDQXJhCN_vNlWCOn]AKxWriP\\@bV|ElDBmP·ZCVo`NXeymkFmMFlCYyBa]iDWWqTCCoa]o·]Oª½PcF{eFËa[WpgZeEyFwgYoNWSMoQQUD]OgAuKIMaTMqmIFcZYgSCEse[QqMyCseJKWaD@YuoWBiQKSFag{|]MeZYvAKYvKIJkb@xQ^wIQu_ouTBgg}a[@ySCo}aYKsEyHYogI[LYYQ]@qbaLoeMsLQK\\_EeIU[TSsMk[mMiVC[]uWIkPeIKQJSI@mcmMALe[a{diSIHTWIVaf[aOiXEYCQZwCAsQiVYLioCsValSHXdZX^Wd@pajRĊqZk}WkZqYSK_TUiSJop¯\\o_gHw\\WD[bCZ]xCDcMSXiFkWoag^KjbAFkZGXxpf@AovKN]m_G¥goYqS]OYg@OEKoqccPAl_WhoW[AC]_IacMRc]L}uIekDuKIk`_IPqMGFqru@Wq[Na[JgueguiaImTBTkM{ggsNuC]WYBO\\kFgMcNUeC~K`Pb¦RjMdZbDpXfAXatRt@²hnARkTQparYVEWWDuUseYcB_SqOyaU|oQQb_EWfcSRgÃjShCZmZ]cS`MYgMmyk]cVMg@aW@kT]WLiouTaeaVYURcA@_kQYscLIhYU_byigsF]QgoAR{kNElYIZgSJ~ib_GeleWYTEAnhlKdV£u³z}lIRwplXn]jN`A^iTCOl_dmR[WUZDXUVh³ @TerQHkklKnWbÁØãĚ]ræDTEÆLbQUdaH]x¿ª]jk¦CjjJL\\Z@PdT@ltpHdR~FdLCvNjOfVpNCnD®tLZ|Z\\zX~AhUdh"],
				"encodeOffsets": [
					[-107805, 21169]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Mexico",
				"iso-code":"MX-MEX"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@ZLTn`At\\hl´pZhZJj\\NvxXabLn\\@vNRMf\\PJhUhdfB\\|PvBlTvxcjJZB¤JRcloH]¼JbuTpnXZIALX`An`F|BÈfnEPNrDbcUJAÆ`rXJ^Qg²XXbvVJ\\nG\\[NH^YdKjVnIxYlebFRaxGlrUP]DoI_H^bj]fEb\\ªD]nafKTLvI^\\Jc\\BXe~pvIlLnMbXVmcIFelaVoXm[WU¥i^U`onTfQQoKJi`RrBPb\\CrfJPfpBL`ZAXbMV\\Z_VJf~hP`hF^JZEpLVa^GG{jVCXdPENOpBMbC|N^BxZZJwEsiA_g]DG][FYH¯qjOI¸{nhNF\\MdzjdQTV^WES¤uDbZVhQÀ_XQUcjgFh\\FbcJclYL[^OM}y]Rq[wbQoWuHCkNs_O@Sq{Lwd[Ne^[PgQOvwkQKgJi\\}aAEuÉ_eE­VIy\\AWRSV[gWuoqeF}KmFMO^qbFPYbIhH`cV_D[jwVYSk^\\VPQlXLY^^H]l@hx\\yRDdSHR¬TbFTy`dDlThk HT\\cA_rn|St_GwCL_¡ZKGUROU[a{]{{YFecMoooNU]etSEwxWP_Hm`R_aQmDGRgCs_K[igGGagCCeLq­YKE[ywYUYTYdD\\LC]]E[hYx`Osbc`}iP[^}HK[]YD__U[qBgU[_RKY_Bu]sPkmKksEYu`S\\spYUuUIkk]D]OO[]@_UySekkHWyBisGYO[psFJ|ZhLli[F_geSIdWbA`dM^]TmGYjCZ`fHd²RbE\\m@W\\aHWtiLq^{LaPYd_X"],
				"encodeOffsets": [
					[-103027, 19341]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Michoacán",
				"iso-code":"MX-MIC"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@VfdMhNlEP[ZA^XvDtMh|hNlASnSbJvjhvfIhb\\\\MXr@qvErNHOr`Jl_LJCvflJ~vKd^NQbd`JD^\\BpXg`XBkdOrdLpPF@Zh^PTZrhpH¦n`M^uLBpe@woWYHElaBi]LbhXpElWjNTCdwDY^aDC\\[XGx`h[po°pIjTSVL`ZTYrXl~YlĉriQobc@]XWcYGWkTUbDtjpKUZRjBtxDRYDFZjWbPe\\UbXJSGTJj|c\\bKfBnNnd@TJI LRfJlOXJ^vD\\jUnNl\\tNST\\JVf`F[RLtKfNpbKra^@RZZ\\KhJZpzGtFZLSNHUMkVAdXzAL[dEKRgEqVKKmlOj]bKTX\\Bp\\NUfSn^vcxRnIb[RvdTpfBQp^tjQlhNUjbZOUZ`dARUG¡lKdVruBP_SP[CEcGHZFC}Oe\\G\\TvQvBtL`UxP^GdifGBffBn\\mZdXGjo\\\\^FvKrikgbgPBl]__Gc][fQLc\\UbJ@|bM|Hh_nJL]fBJhCdPLngTE\\EzzTh\\NIE^vVR@WudOtPEudFD\\¤r´HLObNOtHX`MbXPK`j`^`PYXCdCTeXM`JT_¦_^a¤y`eUwYG«rM~YR[]RIW_qBÅVIdaqCOMmFeAÇE{m__BKWBYJmWSovIa^»pGdkIQA£IYdiuwkSuA{OA[ceVgIg[ONeMQ@u[KmbawWMu[IiYYg³ogk[s_BSmYKU_[G[w]KK[kiYGDzirYFY`cCqR]]JqI_r[YnWCe\\Y©AijZFnsºZID] }icG[_cBeVBX[VB`UR{YSl]AoZ]QROeFk`]uqLEoSG]VSO_LNZ]fTEqDYWO­HuVeEsfuCT]SMyQYSQeB]WWP£UURcBWVaFgVYtWal]FIeO_BOhWFQÓ{]ki[SFkrWO[sCY`[A_ZYQq\\a[OLmQSaDYm_qGgJMsKgNcY@{QmBUeSGKySSDYS«LqKSH{NQi@{sZp£hÙfęoDûvDkRqGiNENÃ\\ǧü^yL£EETg@{Rjk@ÃfWVViKY^ĳ¦c^Efap@fUvir@VaÉ¢UZ\\lc~adeX@TfblZBH^XddM|P^knhtcV@flDTzLL`"],
				"encodeOffsets": [
					[-105972, 19421]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Morelos",
				"iso-code":"MX-MOR"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@zF\\ZLr®KDfhDHbHjhL\\t`DQhHCnbRªüMTwÆ[bx^TMJGl]ZTxU\\i`CdUG_JgZaEOra]NPnE~LfEprvXaa[SUIy{iJY|hTWQeYsWKW[qKKccaR{X_JCkL_GmZuQk\\oWuCmcIO^]CQleJcFKODgU[_@cQ}]HGky[_kSXTnoUBQ`eTFTW\\BVsHi_O[cZBR@CK³Rq@@|uWQPB^alUJc~_LKxonOvca "],
				"encodeOffsets": [
					[-101882, 19170]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Nayarit",
				"iso-code":"MX-NAY"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@lhS@ga[[@op"],
					["@@zxEO|ATecaOo[EiViJmZYDa^"],
					["@@dDnIZDSrW^[GWJajmAM[aION©fdBdsJpWZ"],
					["@@dfX{iSQj"],
					["@@T\\XCRglYDmrFGLiVcf`NvGaXPVIdkJTxOYWP`BhQ\\j\\^JZrw¨Er¢fj r@G¦jVlSj[KaDqdq@oZv\\DúEôL¾fduY}HcbYBTghYdDPX\\M\\iS©nUƏ¥WSGkÆÑhñxNĈĐJdFPSlENVtKFYGwfrW¨@`N\\oFC¬CC]Xwfo`Cp{bEX]hNKpVônWAorK_Bi@NGSuWHggkQ«QWTwDiMqReU\\ElPTE~t\\EJc]`hFh]MgwHbeSWzXEfaIRYnCVQLvUO}teVceuWGSwfYHW_wj_di~KIqpWVcrKlVVafMCSVqjaWSeD[pWOuaCSkOsLmkSkRWKWyNY^YHy@gÃecVaUDUMRWIaF_XWKmJUMgb]]_Lota@QimkBVoW_yU_PoWIiZCsy ^w^Zn£\\SJ]U}^geQJoTMXBbMZBzaLiruRITkVixMIhKTeTEf_\\EXeXB\\whiHkWaBSTaCuX@bWBlHlwPiEMXQcLqOBB]¯e[ikXaBu[]tSgWsqmsYKwRGUqoR{GkoaaQBgQ`{tcHskoKkZPOCfySTtlIndhVpR|ªr^pXJZb\\^WrBrnZBOHDpXZQrblEMgÀPthlBb^r^XAbYdHXgncX_]YBw^£ÍvCZò@dZPH\\`RWTa|pbPhQnJX^@DjWrCz[nKMkJ{¨I@×IQLacsEosÈ]K~PŖTBPçNNOVpTdUrÌX|xAX\\NBTFdb@O|\\z`YpRUdRl·iAO_JkGkbkNa^DpqpeNOQDvSBGnJAf]¬|]C~JC^kfXTMZ]@kcBKLZ"],
					["@@jiDmmXLtKL"],
					["@@\\B|Ç@T^G]VEN«NiG[ZuT»XĻLiA]ìY¦c¬wÀÐ"]
				],
				"encodeOffsets": [
					[
						[-108830, 21828]
					],
					[
						[-109050, 21982]
					],
					[
						[-109220, 22214]
					],
					[
						[-109248, 22272]
					],
					[
						[-108187, 23122]
					],
					[
						[-108195, 23040]
					],
					[
						[-108298, 23073]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Nuevo León",
				"iso-code":"MX-NLE"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@TMjXXbU\\B\\g|ǕŉNµƆ¯D¡VĥÌƥj[RmëũPFxd¢BNYC´TU_YKMmWeNYCuuWcH[ĶKŢËQħGKI_ñ~rĔÝÀLvƏVQhthQxVBVZļŕàâȦ{FǩDơAŭƈ@CÙ­ǏţǝŭkF[I«FLZ[TKreFYMWgIBUYgLMuYWEYl÷£­fåoQéASkIg_uCsE]WYLmewJ}caOI]{V@āykZÏìnr­l³BIiu©yÓOõðß|KBjM}~āZéđsYcGÁdïP@±S}Ñqĝƫ½Zu@H@qeÊu×eHė^l´[Q·Xw£sCLVÛNOeFŉzGjxŸµ¤ELhØFa@nQTCpmBJT|Ah_CWXaMG@ÀhFlqFVSBFz_DjĚd] f@FlPÃvjHP¡DļGªěúµĄIPż}ÖDXĊêÃ²}nfƚĂÒĮfÀ_C°]]ÜmLjQKVIdwfhZWSCc^jËY§HqLfÐc\\XkBO|}BoNJpVf^ÐKQdµZ_Nkj³I}S§HM_rC`gxÊ^TL^¬WzD{TBªWÊuV¬u^`w|a[bãìkrņČƤŞŶłÀķÜÔ\\¨Vn¤ GKdVnDrMhN^MLNAzJTGnĩziVEjcF]U_koHDcV]IOklmG][_t_FGĸƀŞɖºÈʔÄĊÌƠƕdaÆ¸øĄ"],
				"encodeOffsets": [
					[-102239, 28437]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Oaxaca",
				"iso-code":"MX-OAX"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@Zzf`Èv²K¤ÂSzLh]ReEH`Q^Ef°XV^EAWLjbrVV~iE[jIY@DvTjzgrvD\\^J`ZNPfRm¢W¨fbIdlbTebeXNGRJoVElR\\fEZFWW[Pk|LvZtdBcÆ}ÀNnbCO~UH@pUNGlSbSFYnGilcVUES¢]FEDXSxFXpb¦@vvIbnhChRXU|\\B^fxlu^}BPZrJXlMHF~tNjGRJlMhLtSZHP]XL~CWgVQDm`@dQRQ\\L^^HlbDzZCrPVQfCBj`Yd{\\uPgKWUG]an@pP~j\\IT}Ve@~\\nTN]d^rlNI\\Xtfj^HUz@VT²PV[N¡BaeoXsxoLL_^[TZXC`aA\\bZz`E\\TJtU~Vf\\Bn\\hEªdVUxXIbrRL]xyVJd]ÂRDfXfzCXQhBXf\\E¼THfrG^dA^rFYWZSXLxBRqVB|jlG]CG`iH`fYVR\\`gR^Eplxajjbw¾ªDªgDijÁ`icJqzcJHcYWheAi\\y\\\\VEe[@CaRU|QtRTr~CfrN\\IfbErV`EFWlLT_ÎKƯ¥ėIWO@ueA[cCszWJpYZgpKJcdHZSFhCj]RZIHxZWlDTZtPzv`LYxf`DPXV`BVTZnfRvDJXlT¶­WaCQ[KcQHk\\AƪȕĖvcbVjUdEDUsOJq^Ww¿ʰ_ǢUʆ_ɘ]¿Žīʋÿȝöōd­UiRMGeS[]KJugÐ_AumsEAMwAGVqR[GkHc|_L[OUVS|op_HuK_]JUQCi®ösPQ¢QxUPNnWTBdUU[^Y¥c@Cùđ¦Ív«fģ­ZÍXū`AÿP£CMcR{Ie]OIÕJwq_H[Q{BiYA]eE[SHY{[uGgSo{FiIQOoMUmA_UÙSUUeCa]gEmYG·W_[FWyCÙeGK©UwSKOMqcqIwoAAewS±}eEaYDsE]HKÅ^y_oMESkMQHHKKgRXd­hRčR]Lx}^ZTpDN_tYIyZynb\\Ëd[A³RsQL_AR«bIV¥Fg]ñ\\mV½ġÜĻÊ¡p©lmd\\½ZāV\\Ý|iL"],
				"encodeOffsets": [
					[-100917, 16708]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Puebla",
				"iso-code":"MX-PUE"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@`lz\\HlG^R~d`@V\\ChLPdEfIRk^DP]dJDnXv[pRlYvHnK`DlIW`Q|dbLdL\\rXXLZtRfSX{gIZ|jJzTV\\bb\\hUQTX [BJzUF®`fÊFvbB[~IjLhlRuxRP_dCldvKj@ZXWXwbyd_Xg\\BhUEq~}XbMP[\\KWkfSXluMNivQ~mhTxjjz|lVbdRTjWmjbdL@^zXVTg¢Vbf@F\\dHjG|cvl`WrH\\WrRRbXIjt[NPbZId]Hw|EZoJMlgB@VmFBpgY[WpXD\\`\\FlS`}VKZMGdonË^JRBG\\_vSDEq[EOmSOU`C_]N¥@WbEvdFnhfBKt[VeA]jA^]XCfgL[G[RxdRBjXCJjl^VdlOb@jf[hVrctZTR\\NdZGHlPVXDjwNTyzk@U]kG]cuEUfE|dRTvjLJOKbHCbOR\\dVvrnNx´|Lp^D@dR^Hp®d`JnTZWlAjSNF`dbsZf¸rºpAVLT`MZb~dorC[]\\gsSWN[Ozĝ¢G¨JrCxWP]¥u@[e¯HqQMu@CX_LixgG«t]YG[peD{yMZ¹UaÀ}|wfbN^|@EkRw^i@uhIZrZE¦Oh`^rZENtKfM^hjEEYNkǜÃyiU}AckKI[NYE_cI]kkWO{Z]qgY__BqOSFMcJe[v_ig[ZWYKwNgDshcVrDnSBSeGGinIVuZDV\\¦EMrYRYZBHGSZbQ\\DN[I~dlC`mxAd]gHYocy[EO^oAmZQY][LepT½GgLeSXYeQo{kUQDS_Ced_V·P[E_{ImiWq_YCiscEYdWY§PWCglkhTXeTGTqhAdblc^HrmtSHuBU¯phL\\l`ldvgx\\Z]dEXh`Kbéli~@@YqEB]cH]qGe»S[FWegAWRyDWeCeÁQc^UIwzK^qQJawWUV©cgFm[[AUe}sVSI[_FYy[aB_bWDSY]\\K`KwpWtpbfAM¢U\\±OUSy@V]GeiWsJ[kM]q^cSM[m@}f~UJSi[O}@obmH^XVhLOv|[Zc_AieDURqOYDCyka]G][KRQcR_@CnURXh}DWKO^YGsTgKkNQIiHMsE}NGWkqIOYA]~kvewA]{[WVgQgDamuJD\\_\\bD\\QZCl[N@[JoAKeGaFws_z[O]fbY@\\WPfZtIjiJgXP·Kfº}¤ÙnBNnAtwXN\\aRcYalL`K"],
				"encodeOffsets": [
					[-101443, 18773]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Querétaro",
				"iso-code":"MX-QUE"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@PZj`EjLOhBtI~JY¾­^MX_Y\\HfrnEXLhiV@n¬IX`\\KjDjXJdEZYvKO`DrrD~pPITDY[¡ZFz©LCQbg`ODYlNx[H{eUGYlUReqÏA]Q_kGSYAsb@RsbycSy[FaKYLUSVmbDRqGocQUP]CYcaMcD]gGaMQwSCkc{LMuwgOIeXa~uFuyGUW_BaQI]{]IUkVoGwoEU]Co[qCɳeF^aiG]`JCpO^qVHkbwEQfaZkJwUmLiZcG]M[\\mHI[uUWa±WhQ\\}ZqNH¬ZVx_f£z]b¥``IStC_vVDCxUT@ WxQLJ¤hA@vWkTWZkIUiLB`]t\\XNBZ_tL|gHDVbV´pIbn^LClfI@oWfH¼ArA`CDpUoÆ@d]tTzHa\\XPQ`HbZ`@p]X^FfWPKfHhKNVnjº]PrzKNeSV]b@W¬BR|jPVOEx\\{CFWqQsxUfdGZN\\O\\BrPzKx"],
				"encodeOffsets": [
					[-102172, 22011]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "San Luis Potosi",
				"iso-code":"MX-SLP"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@ĜùH©Ļ¢COiGÄuOEke@^cię`CEyTAUrEEkg@¿NHbXW`DBgS{InADoRS@mbEg×FK¶£wŷHiŊyfEPMUÜKtDx¤¸W\\Rk³Ę]fGKQB@¡RCKq I@s¶}cUaEkiJgW_Y^YETkN@¹ÈB^ýÉ·F]ÔsÀrÜCcI«ijV~W~FlĸvyPÚa¢¢JhOzÉNSnCRXL`s^VebRhNiĮKºRňmNkXDpedZfBWÆXFV°rpDKĀ@FsrShATehEVYzOjTfkj@NWG_ACcJeiRWAmeO½kuf[[YOqNMY]FSgeO^cyGqdemRBujSrdbA\\mN_]YUQYRSaHaZWT}RM^\\Fu^FH_XQNouOJYgOcYWStqKRaCa_]FK__CEw^BH£PG^kl]EOfIoBb]WaXcYgB_]RQgkDQMXcpY^BiQNqOe_Fg^ckUR{KyJ[GuV^uKgRofgJMUYZ]A}_WFw\\kMCZ_PahRDKyªEY\\¢CZSJO}oqCCqP_uLYZcFWICiLi_[JWm«U@gjWKmFeq[GZW`]N½®IZ} sJgAPKFii_OY[GF[y_cbIs\\]lNX]VRjCDcE[|WLmriaMTNtW^]GyfqAsn¥I{TqWKaN_]S{B[WgBa`QYÍAYSqEg[qUM\\N\\iVE_gEqnFcz_D^aEOV`pcRcATefRZsNKZkDafÝXÝWsHyj}rF\\PoEQMwlHO_M©FmcMjdXFtbZ²zATe\\tbJ^Bqb}PPjjFFl¢OP[vyhcr]XDf`AVuSbDl¨IRèAb¼EA`|QfLInYtSPkmIinUFpcTãIKÑKkVJÇÛcMk\\§WVyPWm_J|NZV«XhþñSƹŘ©¼_lŎS¨mARtx¢TXuB¥nHCPLZQ@[zl^n`èBCmJonE@MĢfȦÚǶƞľĊĆä\\`öÒoȮJĞ¬ƺRoMUHk¾¯ĂÀĜİ"],
				"encodeOffsets": [
					[-103160, 25048]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Sinaloa",
				"iso-code":"MX-SIN"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@nYrKm|qpSZ]xWX_ñ´í¢YZ"],
					["@@hhD`epebE\\aC[^yIYPDS[aCi¤_ az{d"],
					["@@[lYlr}l}puf_VV[POc[TC`a½Ĉ]zox¥GT"],
					["@@nYK]]HCp"],
					["@@j]zDDoNA\\U`"],
					["@@pd~ÂFEÊSvvzSlva^|gHµApTd¾ÊhfKV`ZpK@@²lvxNK^PxxRNZ@LfpF`dkjtt^¨öLßXG^n`EflFVYXFzQJOTl@^cdz^FlojhfTb_JZyAU_WYAO]aMR{Godg~XEIw¦FÊÓUypFRW[}X[Mww[J]Z@xWRiBW|oHWG@gAIĲƋD{`]zDj_RtEPhfSzBdQ^PPrKlep\\XZI~tN^CÐhel]CyKUBKGS{®IW¬yŖÿMd]BUQhptcC{wMl»Û]U]TAKeAuVUfDR_W[OuIsgùJ¤Ëg[e[ArKőÀoVÃdKxenkbON[gObVMb^INKŪũTMbJsÍnGCqOxgQmfqEU¬ÏL[¸cNÈhTZNzf`¸\\ØmrefJÀ­^_J£vB^tAs`[gPs]L[fI{GfUnQucHË|OËn£NFZGSdOS[Udă`R~NRLPJi{HgRMRxq@]]aR]|UoXOTPSBiR_rAdOIadqIkLW_·lkv[AVQfLLZzV UlFfGpiUH¥q@i¡eFqx§YqI[]iR[AgO_XZPSwlIJcOUbWHMue_dUKjHqECnkZQhTZHjGrHzkpEn]b]Gq^SAidEviy@UZeuZ_fULUXkIifW]eEsf©AcWKojQxc|[Ao¨¢yÍÈ~rWbËâ ¯°tehWJdhn[JVgLmfAZJWuUBAs`Yh@Xq~X^MbºY\\ïØD`SLGdW\\shh|iX^h@Vm~{zHRwWTQ·´Ç¼ùÖµudRJ¾\\aÀ¥Zbtåº¦e}wPIbNR[haJMoNcû°QTpPVdMJZsBYRgWB}[_F]Qun³ĵâăº£v`PrZG\\kzO`ajIpgv[VBfw`@WHaurPlLDlVTE~Êkr`mCkcjg_Y|¡jSAQicD_aFXjWLQfáTamBw[hj¡mfgH[pvA|{l]T[pc~bFX]hkFLp`_Z\\BhUpkAdjRlKZLXKtwWqdKdHrhpEV`[PTZesWqH_MK~VXAPqR]hy\\EiP³¨_N]|sXSjaVqtKzULoM_dSUqCW_ESbWCaYS[Vpy^^P^QzqFq\\aQo`kmNQ^oZinQfSB[~QPY_EaeyGgNeMVoCmPQGeeWE]cGSTQOq@IaBWJitdfFHr^HVRLnQPVxlCfVTbMKRDe]oEe{GsHgOBzShyRheCD~kP[T[E_`]O[ZC¤PJDk~aCKbeMaDOj[J{¤FWB@mLbhFA~XfrbHX`FFkXHk¼bPVEgmBIoýFUn@XjXlLtXTChN¨TSX`i`opWb]nL]rAVpcIcNPb"],
					["@@dÿÑ\\HLGSMJMrUÒmAO"],
					["@@Å`h^|Wi`]Jc"],
					["@@YzzCrBm"],
					["@@KRK®Kpa|@dXTRPSCiDL"]
				],
				"encodeOffsets": [
					[
						[-109990, 24823]
					],
					[
						[-110746, 25575]
					],
					[
						[-110938, 25721]
					],
					[
						[-110871, 25786]
					],
					[
						[-110970, 25802]
					],
					[
						[-111737, 26935]
					],
					[
						[-111146, 25910]
					],
					[
						[-111410, 25978]
					],
					[
						[-111782, 26173]
					],
					[
						[-112019, 26293]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Sonora",
				"iso-code":"MX-SON"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@BxE@oqDfAJ"],
					["@@`H|b^drF\\KtYvH¦NPN´V\\BlnbFVUv@xAHzeD}GbROPQiI§[Y]Fac[A][eIiH_\\aMI\\eD{rDgjcBIXwXµ|"],
					["@@AXbJEL_WkGmFW~@Z"],
					["@@ZMc]KR´"],
					["@@\\W\\¾[S["],
					["@@S˸đǸÅʦýʤýʘûƸ·˨ċǬÃ̤ģɤë˔ďʖÿʒýØ@ǤB˸AːBɦB͌AǪ@ȒAʚ@»ý¿AuoJYbZqC}oSXSÂmDyfI^]ckÌ@BwĵCBčŗHRIñY|Fih{U©B[zSd[Ì[LR|CA[sTDHsZSJ©¬IQ{g|EE\\{rqQcmPUXZRǑEPyQ¿WWRmH¡MI^y]LslSÈAN[]S@UÉA^qvUTkHeGluo[ZpOeWKIaoAVuH}ďY¡DiW^Ga]ARubChmXkhQCaWAtŏQ©jQLipxFLWiKD{n]UA×H`gFkAaR`BD]QSMobKUyv©YeiRD}}a­|MWs@DywUuN¿IL[XSWT£KFYXyUi^cUI^{QLoN§U¥F_jd×`SryzOZcr[DT]asqISqtÀ@`W¨wH{ZWIw^bWTm¶mHMG¹KYzJaAyÅykccDgflmVEXFQ[HuRSj]¤SàWK§õ]ssli_coEKeY@MwQOwL]wMku@±@oLYU_eLÉgc½oS¶BhG{b]kuyTuuÉTEFÁc}oyTQSPDfeFe[mxYLMNRUhWATA»GDBCSoq|gbu`qihs`MQ\\w@Wxq_IteAosCS`mqLeBR`e`OqfOfAVQÇJMK_gFß~«_be|MjJx[NKzaqNHbwlkV|cnG¦O^ÏcBEZMCy_On§Z»\\FbX]PXfiFWVWKnsThe\\yTNjGvqZgSHvb`gdCpihA@gYcHÌte\\JS`]P[BV_LJhMjYA\\zTRXC^ujdfZKTpPmv^_XIbÄ\\AhkTWfRqKUfYHq]bPJssOYqJMYF[|RGWIEa¥QYgv`bG`kJWv]Hy@§uWJOfmd±d_zifiP|UFSpDRYjcLeApS\\qVMjYPCpaDQ\\}h[|FjVC\\iRoA[U{lbVÃ|Ç^MgpwÒWbsjtmbkltQrc^jHjVTEb{`B\\Z@hT\\@jgFA^_H£_kVY^gT¡VGl_LFZvQt[TBMdPTaIpDr_FjQnywDZxP~Alm¢QTvG[YgFsGyXSXQvr^HancFa^BtMZsn_P¼Ebu~y^KzY^MrS`DrI`sxPYMkXRxE^RW_lPĆAnQv[p\\WVHrBW¦emz~«Ö¸]Y¶k¬^V¤XøFnD¾eIh}aKyVje\\µJ÷nÅVYP[Eû`qn¹PP^HvMrgNhQxuxq^Ë|ejCdM^kWSfcDMZc]PWd]KLwG_`mg_G]]EoF×@·hg\\`{dwnUb±|bpuLvbNEmF}\\ata\\}NiK[Nm}BeP[KUbmRKdiRCàVĜ^Ç®W\\VrDIZ^PM\\|~OHPDhPeTDV"]
				],
				"encodeOffsets": [
					[
						[-117426, 32519]
					],
					[
						[-115286, 29570]
					],
					[
						[-115313, 29405]
					],
					[
						[-111864, 27033]
					],
					[
						[-113193, 27944]
					],
					[
						[-117569, 33274]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "South Lower California",
				"iso-code":"MX-BCS"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@Nx`dXHB{X£DwKMsXIhYj@X"],
					["@@`@t[JoY[kPChSp"],
					["@@bdAhTxAbXtrpJTAvMvXATSVcF·krgGC_s}Akg@wqDIv"],
					["@@`TMZ\\\\VABuQ"],
					["@@o`{XF^lD\\]@S^g"],
					["@@v~´]bWDUp{£nPL"],
					["@@df{ViWw_GWbEjet"],
					["@@sFnpFu"],
					["@@Rizf\\]`nmhIiYLH{Utg´LdgBlRz"],
					["@@Qu[O¡ã¾§vY`Å~\\VlOB~E|JtarI~iPqfoZQJsVQ"],
					["@@sPf{RNEUN`^MnNzF°OWÀw"],
					["@@^tSTcI[[Ce­qFHvwbbluHMhRZaZ"],
					["@@htS_VHh[D"],
					["@@J^XPqlkÊ]XM\\eTmGdyDWds@W{D«]BYtWJSQTQ~TIr"],
					["@@npL_MmYOQn"],
					["@@MbPTfºHo"],
					["@@cFq`A`_|[Z_HWpYHLrhGRbE_punhs^Wm[M{hjx ĢĒŸ`Ô\\ODkµ]Im³baIwPg]COoFqWUT§iFSYWTeKEgbKwvu@_Fq\\igsWM\\s~Y·Bc"],
					["@@®BjwK[tsXVV|Utl@^@ͪ@ǚ@ɢ@ǚ@Ƞ@TUuJUs\\soTcAiROJsv}^WdBlhONenOKXO~QtotM\\B~ijO^_BWreNg@uLWDVMPcEc^gbMNolOZ|A[]q¶«nEpg¨[ªQfi@LsmAaWeqSJtaB_d]hURiHy]_eS[oXgDw`U|YZm\\wfx~GXwl_LQd^CRcl@MbGSLMhE|^DTXvWHSt[d]xu`@dg`]pITUKJM¥Zi[OvLV[BWUKiJaCuPWlWJkXedEhsDUGEgh@{\\[VVehWJ}Sg_cLck@fqBoN«TAXeCuHYzq`IxqVLZoIO`@aV|`ANkngB\\[XHEKWRo[k\\PGJwf}DcyD»VTKRgPuZaHabatDpcVmBuxfTQTVsboK[EwaiM{mUgEqOg@LqGOR§DcOY@uTWFzVIJk|tWL[lcÆcÀ[¢I¬HCSa]X\\My\\eDgnQZPr^bjFWJJ`_VDhOVWtEQndHRZÀOAwfQLUfIdpIzmUb]ÄRLMN¿PK|GhO¤T¨pLmjeUki]C©Pa~ut[BMQqET]PG]`ga`QbCxe¼eJ[UNMS_Aif}VMVc\\WM{H_^ieQL_B©]BUWQu[Um[OS_yoiQSUmCÕo{Kw[YsI}aaaA[¥EYQJYÁCwZwj m]QKaGlcÄYègvUp[`IdaZKry¢_dQbusp{l¹~ó`»¡snēĦ»¸s^YfÉ¾z{|ùº»|ÅŃÈ|wZ`@Xgvmflc@EfiDat@dY~YdbXlbL}WWwT[aV]M[ZPzYZCX]QqSBN¢C^wÄgr¡¨@^]`lOZLbm^^§YQuRVooDQD^OTZ^E^]TaWAq`µUAgnB^urLZGlg@GfNbdnD^b^}nRNAJVcL[gNiSKUgHcQ¡]RJUr`CRPGffZ¨u^ThIZzSNRjZFlvNGXĞEU@GnbnF¢h£CJ\\DGrJ²IfJxYbDMz]S]ºkQmY_°o`sX]GY\\@dg¶a~¦ebfuMGgaG~b_`aLsjgJsSIVw~nÃ½ª£jdH²cK]la\\d|}XwF[\\{PUI¡DbYb^d³ÆaXYlGnBeI_kLUJShzDC^²PTNMpAzW~QRyY[JsO{@im_[C[T]yJCukCi^pTMrkL§GeODgsS]oU@i}MvYBId[^n}Ii^Ilm¬]zmxwjr£`uH}MKgiA[Xm|YR@XWlwxlqP¹PuK_cz{NWXoLcYgRPXKfoZkESXFAÊIUsnOAovVdqJoXmDmrUSdtuHU^FrIZub_Oasq\\U~hoEqCb]pCZ{vfZlEwG¨RºIgNrSEb_@^O¨KTD|[pLú vtpblNrUGUSDiK}tlSpqÊk\\s^W^A¢QlrL`] Unht[f]FvSOOU]taGlZ{]a]gA¡zg`@^rPUTQXltzXp­tcHwCcWcgUbT¾´"]
				],
				"encodeOffsets": [
					[
						[-113464, 26275]
					],
					[
						[-113724, 26325]
					],
					[
						[-113902, 26470]
					],
					[
						[-113967, 26727]
					],
					[
						[-115786, 27250]
					],
					[
						[-115983, 27343]
					],
					[
						[-114797, 27895]
					],
					[
						[-116963, 28406]
					],
					[
						[-112565, 24958]
					],
					[
						[-114375, 24936]
					],
					[
						[-114118, 24911]
					],
					[
						[-113048, 25108]
					],
					[
						[-113064, 25157]
					],
					[
						[-113374, 25656]
					],
					[
						[-113388, 25890]
					],
					[
						[-114819, 25894]
					],
					[
						[-114747, 25130]
					],
					[
						[-116944, 28672]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Tabasco",
				"iso-code":"MX-TAB"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@D^_rHYC\\vMrcD_XCT\\bT\\SŎBAéLQ¥Bi~·_YHQ@XM@WjcCaapXh@zn]\\HXuKtHlOjoÂUDfjmĐ¿rMJGhLKbXZCtTVG\\hBX^QfbFI^^@X[xEiVUGLWzFMP[jM`agVglWAXJzEKYdGFpbKMYŐA@ūA˅BȽȃ@Ï@ƉAZzDpXJlONjWfiRCxgTcQ£rWCgTAXiFAk¡`CRyJ\\PdcP~rFr^^KpDpK\\e`T|QhFd]Pc@dNa`oHWVj\\fARxAhOjBdRfaPcaiPiGkZYOEkqIG^djKTs@kRW{WPuV[SUTaeBs}LYkCA^gPaeKR©[V£TYSB¡QYNqµwUN©ISkqOcNs{FwW_eUcEsY@qQ]Aak_uE]`HnolATKqFCN~JLTU iUQwT§Oc[UnYBSeRFbU`sfIJ¨GjYV^^F^zYvcb§AOJ[q_~PnZ@V[TcAo\\E\\LI\\gNoWUD~ioOkYHe[m@]\\W@UY}ykKSomFMczMJe{G@Sq]QwsIMJcEeW[NiOuýĿaPaILj§Kdn~`AfINRzNLMdTSZcCYFA^lSifÇ|}JQ\\DfByCEZ_bHVW]l{WkLQvuOeJHnWRXPNbgRtmh\\X@XYTIfNzQ^]MWRAòvƔ¬\\RæzŨ¤ælÎ^JĲLîBĄF²U¢@ìNÀh¢p¢®t@LHQnHTHºRRÞl"],
					["@@ZhZgs@"]
				],
				"encodeOffsets": [
					[
						[-94688, 19099]
					],
					[
						[-94937, 19021]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Tamaulipas",
				"iso-code":"MX-TAM"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@[ÏWéGSD^ĂNjAxH~^p"],
					["@@ăÛɛMT`pGZRZVH|PVDlĲEPjZPI"],
					["@@N[lSLVrMVQM_vQ^nmCatX^UVqd[UgKvDjYDQaPuIU_Asm`[vBP_Q]`[bPncEwZ]LYmP{Ue\\Yq{NeBqROrATafcS[JkdWSPcKqXYl@^OX]NwhyFiARnV»@oWjk`wg£x}l@l`jMP[lAZRpKCajLWZ]~T`OfXvSR}^KrNLa¥jM@]pObxJLm^MdNTM´OHFgº¡vB\\TR{\\e¦EW_bQfifLrULShTlY`ELVL^Q\\RL_UJfJYRRhQ`LL^UÊ@G[fSbV`[LOº£LefJHeZSVF\\WtQ|LA_ZidMGnJ@ZZpOrdxpY¤nDXxGRPDbERNjAPQLMç_ýUuÉËùų¹ėÇoJL`jvNhTNBtX@Z|_@eOFbbnDbnifQK¥XyA_UI@WJopCKg}Oŕ»Omm{I]NG]giF_KcTSmUSehSPflkfUNg]DQgeoIAcyc[ZeWPWO_lWqTQD{g~cAaa·YÓWÿE¿Nu@kKoQ¿MÁM¡Fock^G@HĭHUDOHg|^D°KÞJhEN¤CxJTHÄGZPÈAbZvWŁ[ƽDƁKŅaŽUœA¥BÛHġF·BÝMąI_yKOscKuOeskIH[d[dqDJ_KeJ{fÕ^`jwUi[Is[I^mtaNF`sp}Pbven}DSL¡KObwT@`f]Cm^@^grkCmNE[mMK^cCs\\e]WuS@JuiSZY@@fW@ceObqGekMiDmMfOoC¯qEUÅWXeAcYofWCMlŇn¹QĭLMjgQaUf]_tKWQmDMTyÊgP¡Ib¡ÙOuzķk}E}XiUjJ¬dÛD¿qÓtE^Ê¸]þÇA@ºlMSZFZ]X`IhljbFdVµ~@tJLrQD@¢RALvØÉrf@Gv@¾YƬĞr~Ò²T@ðOÂcdHtZêĒY}Ă~iNA{LïàPözÔvªJjAk´q®ëmYÐzlĂ @|UJ^PdbI~fxnZK^XFtvDh`lJBTêpReæ¤®køFZZXNvhKZAVhJXZNfELq\\SKY¬E\\JlEǞŮǐŤÚ®DƇ@BŮCƢEǪȥ|ßáĻŖYAUwUgRgsRUuƐ¿KēÞq}`òLJHRĨšÌĵLG\\XdvDvMZXfNnZLV`S³DMZ¡AwcEOìŪQni\\ËƦUĦC¢ƅ°M¶ǖŊ"],
					["@@cyõyÇEtNDO|d`d@^ZhBXjjI"]
				],
				"encodeOffsets": [
					[
						[-100025, 25065]
					],
					[
						[-99888, 25560]
					],
					[
						[-102121, 28344]
					],
					[
						[-99766, 25840]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Tlaxcala",
				"iso-code":"MX-TLA"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@fDRQ|FfdVd\\FfeXZ`d@f`®IdHQ¶ÜB^URD\\jLTIzDdKFuXa¦@M`^DV_TPPn\\FFrTC`uH[AIQÌ]pmHcNLY~UT_Ek_[C[WXo\\hZAonE@UhANkpIFYx{^GJcYOa\\MisWJQaqQ[XqG_Xuk{diHcGE[e@Uah¡USyW@]cKianXSicQUa{kiywigS}nuRMjNvWkeTXl[LO\\aNW}~FrgV[AWhc`azWxWXYi@uLkccD`Oh]\\Mfc\\Kxr|@T`P"],
				"encodeOffsets": [
					[-101033, 20077]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Veracruz",
				"iso-code":"MX-VER"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@P enNjClNfrHPadfX@@eZ@TYvjI@vT^X[ftdDL]nNF\\nMlDhq@]n]^De@_xSPa¢LTK~Cfmau~OtoE_bMns]\\JJtj\\xVQeF¥h÷SCZ§fwrÇD`EHW_QNqfJEWP_LffIEfn}NELur{b@NIcP­`LsmNiE_Te`@@wP}j«x©ñ ārzFejDcRc@^§²¯àÍ|rwêë°«bk®«fsøĕT­fYX]AW ğMiZoCnI]P£pqJiSOC\\G[bpss[fUHedWrDEaXM@_|sUyPheanAPZaHLhC[XmmlU¾q~Ou|]aQºOĘMä@ÚH`MºI|H`g_L]v[dAJ_`UZr[YÚQBExMd[ZiZ±~wLuxsv_hgRq|kF`baxc]¨K¬LRSa_BiKINFEUNJQVHdgbNFh\\lGt¾LRƜBXQ^NR]MyJeZS@W[WngQshMaWOQXGmfIvPRulK|X^kXGU`aFYzDACeR[~IÈ{jeTkB]ZEdDTYSNcMKQyJMe_Bm}Lc¨KibJbO}quGD}wAKO@uOQLim@aQMuɗ^ʅ`ǡVʯ`xÀ]XIrtPCVcFiVaUudĕƩȖ[BGldRLR\\bDXµ®kSIWuCeQYmUS_AUOW_CweKZ_yusOSYkCXwYGYJQi^gDEYTcGIdoLYhoZIyXDt\\dfB@vXPJ¦ĘưÍLS`kKEX_FqUaFe[JqMe}DSqsQ{RQVDb\\@FfU[[[zBjgfZXGdIydIrd_jiÂCj©h©C½xiabikwFoQ]_hQ[ZU_ejGH_^DHikA{rUQwAWKYTZX@Z}@kjaê_LWgcFY^w[uhkc_kK[og°AVGvsTqn]GkdcagBSrSHWfgSklDhOXZ¨cXFZtdDj`ZXrnj|JF`O\\U¸c`DfT`CRlVp|fRWZfThK¾HSfo\\KZ^YRBn]pFPz\\pdGZhc^wB_nkD}cJM\\[CaRYHTGYAQZqZN¥FU[YCUvmJHjHTfAmTqCUgdCtMhLxXZ\\Yjh`uf\\INdEPTAr`Z`rhY^P|lX^ldJF`MZJ\\lLBd~jVzǛÄMlFZiF]geNLMsFqY_]Pg¥YFYqgJ@v]jQxFl{@M]ea{x¿~VbYºzNC|ofH\\^Zs¬hHjw`KDWv@RNGr°\\fv@¦O^wXqD§IH¡yĞ\\PXMtT[h\\^qDp}cYa_NSUKoBq¹e·tYcaE_TMBiXkSYImc_­GogPqÄVZEreLw_DQsc«mAaOUg_AmwwIwaGcYBm£NSEyVY^H^Mj@EXedO^_XV\\vNphTdH^XTMfJcBPxnNVBftbRdeAmeUwcWe[tl^GXhdLjbQb^Lz\\\\V`KdVZ]AYtQZcq[\\Wdb`_@~uNQrBrVtpNYh\\bVKh\\JA^XdfKbZJ\\GfLdxa`EbeNmi_X}NdvAfgScHm]GW_GFqI[daPMyfZH@`}FgIml¼Vj`LDfYnqOa^AJpeFPk^l]OHG¤]AFx`DL`^E`DbQbrLTsZXPdhIZvPMpWRG`]EEv][QNS~YXGbTbZQVRZ`^M[naBqciTAvnQfrczH]dfPhETZ^MNPr\\Ze\\lv¾fPBnQXfjIDdBH`MXi@eliSyPUZgFSfgBqTEtÿ@L"],
					["@@hÒę§²±yÒ­xebqIWF}qÕq¯i}YuQB]@QlJMnLxVRz°fvF~WrMYX}ZaX[|_NqrgcjOrSX@jUXuÀ_`_"],
					["@@gjoPDh_T[j|S^sVI`"]
				],
				"encodeOffsets": [
					[
						[-101038, 22944]
					],
					[
						[-100050, 22497]
					],
					[
						[-99953, 22240]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Yucatán",
				"iso-code":"MX-YUC"
			},
			
			"geometry": {
				"type": "MultiPolygon",
				"coordinates": [
					["@@[BĞx E[Gy{"],
					["@@ĭĈ¦iBQX]ZCF~¥JSl²SlBUBObL¼űƲQzBPLDF@ƓǘwQuSO@y^DsIBr£RT¤QBCtčGKXDLɠJphZlDtVXFnXtdJFThlXHHf\\R^I\\V\\lnlAZT®~|L`XnOfPX^dSXbd^MbRJ\\Òbf@TK\\VdAPfG~xDÌX^JàZÖTĪZàNLD¢TæVNFRx@xLd¶V¦lv\\nF\\lMRH`^P NnLzB²bLdN|B¤]®HxZtBhNdzejHx]¨]¬KXMZSfAHUps|SFK¬QJh_ÔDQRwHYLµL¹ZċNmXgMiReCmNSQaLqEePJQf|M¤GÈAÖsÒ{¸eÂaVBĶwNQrKBȥBȯţȝÍĥǽǧBD{ťŗHÅBHQiaL³MMgyJÛdCIÍJWaqDMaiH§}LYM£OeJyCYI]@mWO@ec_NiYW@B_kCFqaBDl}JsgIuiJo±®LEmHK}NDR¥TSõtCA}AJ²NAUFHyÝ³"],
					["@@ØfÆ\\JUAOÃWaA"]
				],
				"encodeOffsets": [
					[
						[-92560, 21347]
					],
					[
						[-91439, 20021]
					],
					[
						[-92000, 21758]
					]
				]
			}
		},
		{
			"type": "Feature",
			"properties": {
				"name": "Zacatecas",
				"iso-code":"MX-ZAC"
			},
			
			"geometry": {
				"type": "Polygon",
				"coordinates": ["@@JPrZJf`ZFXMfavE^btdFdzVL_jNjiZB^^lGd\\\\Cj`VfJPb^VdQTYpB`PpI|ZS~SdLTW`TRKox_NE\\WLOBŪ®RĖNlFXxZjLn`þ¶hplHPZJzVudbRÇFTnYvQr|\\NHb_bArc\\EXnX¶]HahN~IFf¦Æ¬fr^P`vxj`MdT~Y|B`jJGnZDdlFtvXGd`dJPQ´JpLrIbJXZhUVelNh@bVPMf@uVZ[ºbrÈ{Rw^`YfHpb}¢ANdT~NP\\ÖG\\bHM`N ]ĺcĆĶ_Aě@~@^|ocnȈÉaĮƘT¶Mƀ¡Ȝ«d¶ąözj²sOVfè[¤C~M¤Vbºr|WÞojE\\Sn^©µ ĺ£´®yGTJ¶ăěįā¿½°GlNVQp«ƹĝIpȭõÑ[_ąãĽĉǵƝȥÙġeN@mFIpDnA_ç]myk\\R@YOKDG¦mAvSWw¡QsnBT§ō`kª»ƺŗòTgý¬WUMY{`IXnOUz¨Xl[dNÈÜIlULÒLäJdSoVEjmnJlTOZsJmKRe{B_»FBaQç§JCkTavBUe_C^Wdqzgu\\POk¡EiEOi~OraAI]saf[BS±yaYEscWNi[UWf_MUpYDO¸AnkC{FaU[@OqjwXBLm^DPkXuMM`gDWIREZaZï´eTRRrw@^^z@DÆmVOO\\N[ccEJF´FHZ@@TDFVclo\\O[E@_QmA}EgqSFÝ[IYR]Q[IIUiEšBC|cIo}{N_amIÃKwico_mi¿McZYV@KjA`rU _wc[aEXaRVsvSZFVehDEo²[@adcquXaceDXgCeuaAKjYf_PGcJY{cDaeKSZUDqkRWSEciiIesCO{RCjyLW`ÉOYa[¥XyQ_cMS_©ifYAM^gPo_sJEieKaPQOoCWY]HDXwxNXeLB_SdcECWYkVYtUJFj[\\y`\\\\HdIhLpkAQRqIucKbBpddlMVHP`FzPf[`DVVHfr¬WX[[wjGjwDSVKfFTzV^SvLff`EP_\\CVTLpWLhI^\\TRPDBzgWCydPZHKZ|`PUZNdZXZvdbRQt^|BNxzdPVph\\chJrQzBIAdRVIp\\DRzlGhZNlfPrIZh^ZDfwlB\\a`B§JO]_KKfVfA\\bFtrCzeL_^]L_[cDMZuBEUaWS^]i]DXWA\\LVhjATOUcGk^O]esZqMkWC]oFcGQaYMPqO]H@hgQm`mUqwoPgKoRgh~XPPT|VRj~gHe\\MnTtzI^¨Cd`HdZUd@zQPAlUHLpYBMfFX_N_£b]Re_qC_¥BemIÅQYskAWb_yBc§WN[¤]T_xLjGtUnJ`\\\\tZAH^kbVRKZBlXANxYjRnZJK^JV|\\LNbX`K`T`IfXdErwVCCLGkNmQWnoI·DG]__GalIRaZL^Qe`|IAgSO`UkcjcaVsO]gWCc]@am]CFeQgLg]{GDfb@C^P|TXBhV^Gp_ZDhUXoCc_PY}BYVmSWDYe`mYSRUhEKgZYEkfS^S[iGGAj`qLBp"],
				"encodeOffsets": [
					[-106853, 22948]
				]
			}
		}],
		"UTF8Encoding": true
	});
}));