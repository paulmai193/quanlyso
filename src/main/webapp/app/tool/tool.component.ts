import { Component, OnInit } from '@angular/core';
import { Channel } from '../entities/channel/channel.model';
import { Factor } from '../entities/factor/factor.model';
import { Style } from '../entities/style/style.model';
import { Types } from '../entities/types/types.model';
import { TransactionDetails } from '../entities/transaction-details/transaction-details.model';
import { Transactions } from '../entities/transactions/transactions.model';
import { TransactionDetailsService } from '../entities/transaction-details/transaction-details.service';
import { TransactionsService } from '../entities/transactions/transactions.service';
import { ChannelService } from '../entities/channel/channel.service';
import { FactorService } from '../entities/factor/factor.service';
import { StyleService } from '../entities/style/style.service';
import { TypesService } from '../entities/types/types.service';
import { AlertService } from 'ng-jhipster';
import { Response } from '@angular/http';
/**
 * Created by Dai Mai on 6/17/17.
 */
@Component({
    selector: 'jhi-tool',
    templateUrl: './tool.component.html'
})
export class QuanLySoToolComponent implements OnInit {
    channels: Channel[];
    factors: Factor[];
    styles: Style[];
    types: Types[];
    transactions: Transactions;
    transactionDetailses: TransactionDetails[];
    isProcess: boolean;

    constructor(private transactionDetailsService: TransactionDetailsService,
                private transactionsService: TransactionsService,
                private channelService: ChannelService,
                private factorService: FactorService,
                private styleService: StyleService,
                private typesService: TypesService,
                private alertService: AlertService
    ) {
        this.transactions = new Transactions();
        this.transactionDetailses = [];
        this.addRecord();
    }

    ngOnInit(): void {
        this.channelService.query().subscribe(
            (res: Response) => { this.channels = res.json(); }, (res: Response) => this.onError(res.json()));
        this.factorService.query().subscribe(
            (res: Response) => { this.factors = res.json(); }, (res: Response) => this.onError(res.json()));
        this.styleService.query().subscribe(
            (res: Response) => { this.styles = res.json(); }, (res: Response) => this.onError(res.json()));
        this.typesService.query().subscribe(
            (res: Response) => { this.types = res.json(); }, (res: Response) => this.onError(res.json()));
    }

    addRecord(): void {
        this.transactionDetailses.push(new TransactionDetails());
    }

    removeRecord(target: TransactionDetails): void {
        const idx: number = this.transactionDetailses.findIndex((t: TransactionDetails) => t === target);
        if (idx > -1) {
            this.transactionDetailses.splice(idx, 1);
        }
    }

    check(): void {
        console.log('Check');
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
