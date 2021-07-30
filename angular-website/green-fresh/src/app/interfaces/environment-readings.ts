import { AreaValue } from "./area-value";
import { ProductionLine } from "./production-line";



export interface EnvironmentReadings {
    productionLine: ProductionLine;

    values: AreaValue[];
}
