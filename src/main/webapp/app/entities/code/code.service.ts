import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Code } from './code.model';
import { DateUtils } from 'ng-jhipster';

@Injectable()
export class CodeService {

    private resourceUrl = 'api/codes';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(code: Code): Observable<Code> {
        const copy = this.convert(code);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(code: Code): Observable<Code> {
        const copy = this.convert(code);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Code> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            jsonResponse.openDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.openDate);
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
            jsonResponse[i].openDate = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].openDate);
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

    private convert(code: Code): Code {
        const copy: Code = Object.assign({}, code);

        copy.openDate = this.dateUtils.toDate(code.openDate);
        return copy;
    }
}
