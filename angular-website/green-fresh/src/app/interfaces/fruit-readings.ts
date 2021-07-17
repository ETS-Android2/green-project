export interface FruitReadings {
    code: string;
    ip: string;
    description: string;
    status: {
        lastConnection: string;
        value: string;
    }
    reading: {
        date: string;
        fruit: string;
        weight: {
            value: string;
        }
        color: {
            R: number;
            G: number;
            B: number;
        }
    }
}
