import { Fruit } from "./fruit";
import { ProductionLine } from "./production-line";

export interface InspectionResults {
    productionLine: ProductionLine,
    fruit: Fruit | undefined,
    results: {
        accepted: number,
        rejected: number
    }

}
