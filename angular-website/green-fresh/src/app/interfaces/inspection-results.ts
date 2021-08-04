import { Fruit } from "./fruit";
import { ProductionLine } from "./production-line";

export interface InspectionResults {
    productionLine: ProductionLine | undefined,
    fruit: Fruit | undefined,
    date: string,
    results: {
        accepted: number,
        rejected: number
    }

}
