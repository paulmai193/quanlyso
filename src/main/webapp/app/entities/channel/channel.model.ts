export class Channel {
    constructor(
        public id?: number,
        public name?: string,
        public code?: string,
        public sunday?: boolean,
        public monday?: boolean,
        public tuesday?: boolean,
        public wednesday?: boolean,
        public thursday?: boolean,
        public friday?: boolean,
        public saturday?: boolean,
        public transactionDetailsId?: number,
    ) {
        this.sunday = false;
        this.monday = false;
        this.tuesday = false;
        this.wednesday = false;
        this.thursday = false;
        this.friday = false;
        this.saturday = false;
    }
}
