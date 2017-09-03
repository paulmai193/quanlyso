export class TransactionDetails {
    public uuid: number = Date.now();
    constructor(
        public id?: number,
        public amount?: number,
        public profit?: number,
        public costs?: number,
        public transactionsId?: number,
        public channelsId?: number,
        public stylesId?: number,
        public typesId?: number,
        public costRate?: number,
        public minRate?: number,
        public maxRate?: number,
    ) {
    }

    public style(id): TransactionDetails {
        this.stylesId = id;
        return this;
    }

    public type(id): TransactionDetails {
        this.typesId = id;
        return this;
    }
}
