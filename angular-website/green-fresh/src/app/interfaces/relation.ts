import { Fruit } from "./fruit";
import { ProductionLine } from "./production-line";

export interface Relation {
    productionLine : ProductionLine,
    fruit: Fruit
}
