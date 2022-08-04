/******** i18n ********/
GXC_DigitalSign_UC.i18n = function (template) {
	if (GXC_DigitalSign_UC.Language == "" || GXC_DigitalSign_UC.Language == "-") {
		GXC_DigitalSign_UC.Language = "en";
	}
	for (var
		info = GXC_DigitalSign_UC.i18n.db[GXC_DigitalSign_UC.Language][template.join('\x01')],
		out = [info.t[0]],
		i = 1, length = info.t.length; i < length; i++
	) out[i] = arguments[1 + info.v[i - 1]] + info.t[i];
	return out.join('');
}
GXC_DigitalSign_UC.i18n.db = {};

GXC_DigitalSign_UC.i18n.set = locale => (tCurrent, ...rCurrent) => {
	const key = tCurrent.join('\x01');
	let db = GXC_DigitalSign_UC.i18n.db[locale] || (GXC_DigitalSign_UC.i18n.db[locale] = {});
	db[key] = {
		t: tCurrent.slice(),
		v: rCurrent.map((value, i) => i)
	};
	const config = {
		for: other => (tOther, ...rOther) => {
			db = GXC_DigitalSign_UC.i18n.db[other] || (GXC_DigitalSign_UC.i18n.db[other] = {});
			db[key] = {
				t: tOther.slice(),
				v: rOther.map((value, i) => rCurrent.indexOf(value))
			};
			return config;
		}
	};
	return config;
};

GXC_DigitalSign_UC.i18n.set('en')`Extension not installed. Please follow the steps below for installing.`
	.for('es')`No se encuentra instalada la extensión. Siga los siguientes pasos para instalarla.`
	.for('pt')`A extensão não está instalada. Siga as instruções para instalá-lo.`;

GXC_DigitalSign_UC.i18n.set('en')`Press the button below to install it`
	.for('es')`Presione el botón a continuación para instalarla.`
	.for('pt')`Clique no botão abaixo para instalá-lo`;

GXC_DigitalSign_UC.i18n.set('en')`Install extension`
	.for('es')`Instalar extensión`
	.for('pt')`Instalar extensão`;

GXC_DigitalSign_UC.i18n.set('en')`If you would like to download JNLP file press this link.`
	.for('es')`Si desea descargar el archivo JNLP presione este link.`
	.for('pt')`Se deseja baixar o arquivo JNLP, clique este link.`;

GXC_DigitalSign_UC.i18n.set('en')`JNLP file is downloading. Please follow the steps below for sign.`
    .for('es')`El archivo JNLP se esta descargando. Siga los pasos para instalar.`
    .for('pt')`O arquivo JNLP está sendo baixado. Siga as etapas abaixo para a instalação.`;

GXC_DigitalSign_UC.i18n.set('en')`Download JNLP`
	.for('es')`Descargar JNLP`
	.for('pt')`Baixar JNLP`;

GXC_DigitalSign_UC.i18n.set('en')`DownloadJNLP event missing. Please contact administrator.`
	.for('es')`Evento DownloadJNLP no implementado. Por favor contacte al administrator.`
	.for('pt')`Evento DownloadJNLP não implementado. Por favor contate o administrador.`;

GXC_DigitalSign_UC.i18n.set('en')`WCDownloadJNLP event missing. Please contact administrator.`
	.for('es')`Evento WCDownloadJNLP no implementado. Por favor contacte al administrator.`
	.for('pt')`Evento WCDownloadJNLP não implementado. Por favor contate o administrador.`;

GXC_DigitalSign_UC.i18n.set('en')`Digital Sign InstallNative event not defined! Contact system administrator.`
	.for('es')`Evento InstallNative no implementado. Por favor contacte al administrator.`
	.for('pt')`Evento InstallNative não implementado. Por favor contate o administrador.`;

GXC_DigitalSign_UC.i18n.set('en')`Digital Sign InstallOK event not defined! Contact system administrator.`
	.for('es')`Evento InstallOK no implementado. Por favor contacte al administrator.`
	.for('pt')`Evento InstallOK não implementado. Por favor contate o administrador.`;

GXC_DigitalSign_UC.i18n.set('en')`Digital Sign onComplete event not defined! Contact system administrator.`
	.for('es')`Evento onComplete no implementado. Por favor contacte al administrator.`
	.for('pt')`Evento onComplete não implementado. Por favor contate o administrador.`;

GXC_DigitalSign_UC.i18n.set('en')`Signature finished`
	.for('es')`Firma completada`
	.for('pt')`Assinatura concluída`;

GXC_DigitalSign_UC.i18n.set('en')`The signature process has ended.`
	.for('es')`Termino el proceso de firma.`
	.for('pt')`O processo de assinatura terminou.`;