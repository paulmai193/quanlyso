import construct = Reflect.construct;
import { TransactionDetails } from '../transaction-details/transaction-details.model';
export class Transactions {
    public uuid: number = Date.now();
    constructor(
        public id?: number,
        public chosenNumber?: number,
        public openDate?: any,
        public netValue?: number,
        public clientsId?: number,
        public clientsName?: string,
        public transactionDetailsDTOs?: TransactionDetails[]
    ) {
        this.transactionDetailsDTOs = [];
    }
}
