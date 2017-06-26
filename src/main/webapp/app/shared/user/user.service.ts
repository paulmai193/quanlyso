import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { User } from './user.model';
import { DateUtils } from 'ng-jhipster';

@Injectable()
export class UserService {
    private resourceUrl = 'api/users';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(user: User): Observable<Response> {
        const copy = this.convert(user);
        return this.http.post(this.resourceUrl, copy);
    }

    update(user: User): Observable<Response> {
        const copy = this.convert(user);
        return this.http.put(this.resourceUrl, copy);
    }

    find(login: string): Observable<User> {
        return this.http.get(`${this.resourceUrl}/${login}`).map((res: Response) => res.json());
    }

    query(req?: any): Observable<Response> {
        const params: URLSearchParams = new URLSearchParams();
        if (req) {
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
        }

        const options = {
            search: params
        };

        return this.http.get(this.resourceUrl, options);
    }

    delete(login: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${login}`);
    }

    authorities(): Observable<string[]> {
        return this.http.get('api/users/authorities').map((res: Response) => {
            const json = res.json();
            return <string[]> json;
        });
    }

    private convert(client: User): User {
        const copy: User = Object.assign({}, client);

        copy.grantAccessDate = this.dateUtils.toDate(client.grantAccessDate);

        copy.revokeAccessDate = this.dateUtils.toDate(client.revokeAccessDate);
        return copy;
    }

}
