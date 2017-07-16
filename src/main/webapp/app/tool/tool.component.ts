import { Component, OnInit, OnDestroy } from '@angular/core';
import { Channel } from '../entities/channel/channel.model';
import { Factor } from '../entities/factor/factor.model';
import { Style } from '../entities/style/style.model';
import { Types } from '../entities/types/types.model';
import { TransactionDetails } from '../entities/transaction-details/transaction-details.model';
import { Transactions } from '../entities/transactions/transactions.model';
import { TransactionsService } from '../entities/transactions/transactions.service';
import { ChannelService } from '../entities/channel/channel.service';
import { FactorService } from '../entities/factor/factor.service';
import { StyleService } from '../entities/style/style.service';
import { TypesService } from '../entities/types/types.service';
import { AlertService, EventManager } from 'ng-jhipster';
import { Response } from '@angular/http';
import { DatePipe } from '@angular/common';
import { Observable } from 'rxjs/Observable';
import { StorageService } from '../shared/storage/storage.service';
import { CrawlDataModel } from '../entities/crawl-data.model';
import { CodeService } from '../entities/code/code.service';
/**
 * Created by Dai Mai on 6/17/17.
 */
@Component({
    selector: 'jhi-tool',
    templateUrl: './tool.component.html',
    styleUrls: [
        'tool.component.scss'
    ]
})
export class QuanLySoToolComponent implements OnInit, OnDestroy {
    crawlData: CrawlDataModel;
    channelsForCrawl: Channel[];
	processing: string;

    channels: Channel[];
    factors: Factor[];
    styles: Style[];
    types: Types[];
    transactions: Transactions;
    isCalculate: boolean;

    private DATE_FORMAT = 'yyyy-MM-ddT00:00';
    private running: any;

    constructor(private eventManager: EventManager,
                private transactionsService: TransactionsService,
                private channelService: ChannelService,
                private factorService: FactorService,
                private styleService: StyleService,
                private typesService: TypesService,
                private alertService: AlertService,
                private datePipe: DatePipe,
                private storageService: StorageService,
                private codeService: CodeService
    ) {
        this.reset();
        this.processing = 'None';
    }

    ngOnInit(): void {
        this.transactions.openDate = this.crawlData.openDate = this.datePipe.transform(Date.now(), this.DATE_FORMAT);
        this.onCrawlOpenDateChange();
        this.onCalculateOpenDateChange();
    }
    
    ngOnDestroy(): void {
    	this.isRunning = false;
    }

    onCrawlOpenDateChange(): void {
        const openDate: string = this.datePipe.transform(this.crawlData.openDate, 'EEEE').toLowerCase();
        this.initCrawl(openDate);
    }

    onCalculateOpenDateChange(): void {
        const openDate: string = this.datePipe.transform(this.transactions.openDate, 'EEEE').toLowerCase();
        this.initCalculate(openDate);
    }

    addRecord(): void {
        this.transactions.transactionDetailsDTOs.push(new TransactionDetails());
    }

    removeRecord(target: TransactionDetails): void {
        const idx: number = this.transactions.transactionDetailsDTOs.findIndex((t: TransactionDetails) => t === target);
        if (idx > -1) {
            this.transactions.transactionDetailsDTOs.splice(idx, 1);
        }
    }

    reset(): void {
        this.crawlData = new CrawlDataModel();
        this.transactions = new Transactions();
        this.transactions.clientsId = this.storageService.getAccountId();
        this.transactions.transactionDetailsDTOs = [];
        this.addRecord();
    }

    check(): void {
        this.isCalculate = true;
        this.subscribeToSaveResponse(this.transactionsService.create(this.transactions), 'calculate');
    }

    crawl(): void {
        this.subscribeToSaveResponse(this.codeService.crawl(this.crawlData), 'crawl');
    }

    private initCrawl(openDate: string): void {
        this.channelService.findByOpenDay(openDate).subscribe(
            (res: Response) => {
                this.channelsForCrawl = res.json();
                },
            (res: Response) => this.onError(res.json()));
    }

    private initCalculate(openDate: string): void {
        this.channelService.findByOpenDay(openDate).subscribe(
            (res: Response) => {
                this.channels = res.json();
                },
            (res: Response) => this.onError(res.json()));
        this.factorService.query().subscribe(
            (res: Response) => { this.factors = res.json(); }, (res: Response) => this.onError(res.json()));
        this.styleService.query().subscribe(
            (res: Response) => { this.styles = res.json(); }, (res: Response) => this.onError(res.json()));
        this.typesService.query().subscribe(
            (res: Response) => { this.types = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    
    private getCrawlProcess(): void {
		if (this.processing === 'None') {
			
		}
    }

    private subscribeToSaveResponse(result: Observable<any>, instance: string) {
        result.subscribe((res: any) =>
            this.onSaveSuccess(res, instance), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: any, instance: string) {
        if (instance === 'calculate') {
            this.eventManager.broadcast({ name: 'channelListModification', content: 'OK'});
            this.isCalculate = false;
        } else {
            this.eventManager.broadcast({ name: 'channelListModification', content: 'OK'});
        }
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isCalculate = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

}
