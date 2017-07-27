export class Client {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public grantAccessDate?: any,
        public revokeAccessDate?: any,
        public transactionssId?: number,
    ) {
    }

}
