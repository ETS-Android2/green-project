import { ProductionLine } from "./production-line";

interface value {
    temperarure: number;
    humidity: number;
}

export interface EnvironmentReadings {
    productionLine: ProductionLine;

    values: value[];
}
