import { ProductionLine } from "./production-line";
import { Reading } from "./reading";

export interface FruitReadings {

    productionLine: ProductionLine;

    readings: Reading[];
}
