

export interface Fruit {
    code: string;
    name: string;
    description: string;
    image: any;

    requirements : {
        R: number;
        G: number;
        B: number;
    }
}
