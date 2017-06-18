import { Injectable } from '@angular/core';
import { LocalStorageService, SessionStorageService } from 'ng2-webstorage';
/**
 * Created by Dai Mai on 6/18/17.
 */
@Injectable()
 export class StorageService {
    private ACCOUNT_ID = 'account-id';
    private AUTH_TOKEN = 'authenticationToken';
    private remember: boolean;

    constructor(private $localStorage: LocalStorageService,
                private $sessionStorage: SessionStorageService
    ) {}

    getToken() {
        return this.get(this.AUTH_TOKEN);
    }

    getAccountId() {
        return +this.get(this.ACCOUNT_ID);
    }

    storeAuthenticationToken(jwt, rememberMe: boolean): void {
        if (rememberMe) {
            this.rememberMe();
        }
        this.storeToken(jwt);
    }

    storeToken(jwt): void {
        this.set(this.AUTH_TOKEN, jwt);
    }

    storeAccountInfo(account: any): void {
        this.set(this.ACCOUNT_ID, account.id);
    }

    rememberMe(): void {
        this.remember = true;
    }

    removeToken(): void {
        this.remove(this.AUTH_TOKEN);
    }

    get(key: string) {
        if (this.remember) {
            return this.$localStorage.retrieve(key);
        } else {
            return this.$sessionStorage.retrieve(key);
        }
    }

    set(key: string, value: any) {
        if (this.remember) {
            return this.$localStorage.store(key, value);
        } else {
            return this.$sessionStorage.store(key, value);
        }
    }

    remove(key: string) {
        if (this.remember) {
            return this.$localStorage.clear(key);
        } else {
            return this.$sessionStorage.clear(key);
        }
    }
}
