import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Client } from './client.model';
import { DateUtils } from 'ng-jhipster';

@Injectable()
export class ClientService {

    private resourceUrl = 'api/users';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(client: Client): Observable<Client> {
        const copy = this.convert(client);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(client: Client): Observable<Client> {
        const copy = this.convert(client);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Client> {
        return this.http.get(`${this.resourceUrl}/id/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            jsonResponse.grantAccessDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.grantAccessDate);
            jsonResponse.revokeAccessDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.revokeAccessDate);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        const options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): Response {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].grantAccessDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].grantAccessDate);
            jsonResponse[i].revokeAccessDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].revokeAccessDate);
        }
        res.json().data = jsonResponse;
        return res;
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

    private convert(client: Client): Client {
        const copy: Client = Object.assign({}, client);

        copy.grantAccessDate = this.dateUtils.toDate(client.grantAccessDate);

        copy.revokeAccessDate = this.dateUtils.toDate(client.revokeAccessDate);
        return copy;
    }
}
