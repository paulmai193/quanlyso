import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Transactions } from './transactions.model';
import { DateUtils } from 'ng-jhipster';

@Injectable()
export class TransactionsService {

    private resourceUrl = 'api/transactions';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    calculate(transactions: Transactions): Observable<Transactions> {
        const copy = this.convert(transactions);
        return this.http.post(this.resourceUrl + '/calculate', copy).map((res: Response) => {
            return res.json();
        });
    }

    create(transactions: Transactions): Observable<Transactions> {
        const copy = this.convert(transactions);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(transactions: Transactions): Observable<Transactions> {
        const copy = this.convert(transactions);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Transactions> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options);
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }
    private createRequestOption(req?: any): BaseRequestOptions {
        const options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            const params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }

    private convert(transactions: Transactions): Transactions {
        const copy: Transactions = Object.assign({}, transactions);

        copy.openDate = this.dateUtils.toDate(transactions.openDate);

        return copy;
    }
}
