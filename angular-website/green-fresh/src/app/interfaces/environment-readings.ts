export interface EnvironmentReadings {
    code: string;
    ip: string;
    description: string;
    status: {
        lastConnection: string;
        value: string;
    }
    values: {
        temperarure: number;
        humidity: number;
    }
}
