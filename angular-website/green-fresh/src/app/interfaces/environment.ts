export interface Environment {

    code: string;
    date: string;
    ip: string;
    status: {
        lastConnection: string;
        status: string;
    }
    values: {
        humidity: number;
        temperature: number;
    }

}
