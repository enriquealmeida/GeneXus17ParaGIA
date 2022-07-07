export declare class DynamicProvider {
    private monaco;
    private configuration;
    constructor(monaco: any, configuration: any);
    getAllClasses(): Promise<any[]>;
    getAllImages(): Promise<any[]>;
    getAllFiles(): Promise<any[]>;
    getAllDSOs(): Promise<any[]>;
    getAllTokensFromGroup(group: any): Promise<any[]>;
}
