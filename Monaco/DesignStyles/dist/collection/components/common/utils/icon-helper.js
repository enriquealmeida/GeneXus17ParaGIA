export class IconHelper {
    static getIconFromGxName(name) {
        let geminiIcon = IconHelper.IconsMapping[name];
        if (geminiIcon == null)
            geminiIcon = IconHelper.IconsMapping.ToBeDefined;
        return geminiIcon;
    }
}
IconHelper.IconsMapping = {
    WebPanel: 'objects/webpanel',
    DesignSystem: 'objects/dso',
    WebComponent: 'objects/web-component',
    Transaction: 'objects/transaction',
    MasterPage: 'objects/masterpage',
    ToBeDefined: 'objects/to-be-defined'
};
