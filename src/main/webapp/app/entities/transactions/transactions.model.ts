import { TransactionDetails } from '../transaction-details/transaction-details.model';
import { Channel } from '../channel/channel.model';
export class Transactions {
    constructor(
        public id?: number,
        public chosenNumber?: number,
        public openDate?: any,
        public netValue?: number,
        public clientsId?: number,
        public clientsName?: string,
        public transactionDetailsDTOs?: TransactionDetails[],
        public chooseChannel?: Channel,
        public cost?: number
    ) {
        this.transactionDetailsDTOs = [];
    }
}
