export class TransactionDetails {
    constructor(
        public id?: number,
        public amount?: number,
        public profit?: number,
        public costs?: number,
        public transactionsId?: number,
        public channelsId?: number,
        public factorsId?: number,
        public stylesId?: number,
        public typesId?: number,
    ) {
        this.profit = -1;
        this.costs = -1;
    }
}
