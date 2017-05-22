import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Client } from './client.model';
import { ClientService } from './client.service';
@Injectable()
export class ClientPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private clientService: ClientService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.clientService.find(id).subscribe((client) => {
                client.grantAccessDate = this.datePipe
                    .transform(client.grantAccessDate, 'yyyy-MM-ddThh:mm');
                client.revokeAccessDate = this.datePipe
                    .transform(client.revokeAccessDate, 'yyyy-MM-ddThh:mm');
                this.clientModalRef(component, client);
            });
        } else {
            return this.clientModalRef(component, new Client());
        }
    }

    clientModalRef(component: Component, client: Client): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.client = client;
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
