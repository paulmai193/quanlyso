import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { User, UserService } from '../../shared';
import { DatePipe } from '@angular/common';
import { DateUtil } from '../../shared/utils/date-util';

@Injectable()
export class UserModalService {
    private isOpen = false;
    private DATE_FORMAT_INIT = 'yyyy-MM-ddT12:00';
    private DATE_FORMAT = 'yyyy-MM-ddThh:mm';
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private userService: UserService
    ) {}

    open(component: Component, login?: string): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (login) {
            this.userService.find(login).subscribe((user) => {
                user.grantAccessDate = this.datePipe
                    .transform(user.grantAccessDate, this.DATE_FORMAT);
                user.revokeAccessDate = this.datePipe
                    .transform(user.revokeAccessDate, this.DATE_FORMAT);
                this.userModalRef(component, user);
            });
        } else {
            const user: User = new User();
            // Setup default grant & revoke access date
            user.grantAccessDate = this.datePipe.transform(new Date(), this.DATE_FORMAT_INIT);
            user.revokeAccessDate = this.datePipe.transform(new DateUtil().addDays(user.grantAccessDate, 30), this.DATE_FORMAT_INIT);
            return this.userModalRef(component, user);
        }
    }

    userModalRef(component: Component, user: User): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.user = user;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
