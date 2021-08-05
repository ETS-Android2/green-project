import { Fruit } from "./fruit";

export interface Reading {
      
    date: string;
    fruit: Fruit | string;
    weight: {
        value: string;
    }
    color: {
        R: number;
        G: number;
        B: number;
    }
    
}


