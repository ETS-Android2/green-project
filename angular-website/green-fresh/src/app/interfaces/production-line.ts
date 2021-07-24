export interface ProductionLine {
    code: string;
    ip: string;
    description: string;
    status: {
        lastConnection: string;
        value: string;
        res: boolean;
    }
}
