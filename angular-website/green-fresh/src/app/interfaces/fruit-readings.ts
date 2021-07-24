import { ProductionLine } from "./production-line";

interface reading{
  
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

export interface FruitReadings {

    productionLine: ProductionLine;

    readings: reading[];
}
