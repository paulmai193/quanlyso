export class Transactions {
    constructor(
        public id?: number,
        public chosenNumber?: number,
        public netValue?: number,
        public transactionDetailsId?: number,
        public clientsId?: number,
        public clientsName?: string,
    ) {
    }
}
