/**
 * Created by Dai Mai on 6/17/17.
 */
import {Component, OnInit, OnDestroy, ViewChild, ElementRef} from '@angular/core';
import { Channel } from '../entities/channel/channel.model';
import { Style } from '../entities/style/style.model';
import { Types } from '../entities/types/types.model';
import { TransactionDetails } from '../entities/transaction-details/transaction-details.model';
import { Transactions } from '../entities/transactions/transactions.model';
import { TransactionsService } from '../entities/transactions/transactions.service';
import { ChannelService } from '../entities/channel/channel.service';
import { StyleService } from '../entities/style/style.service';
import { TypesService } from '../entities/types/types.service';
import { AlertService, EventManager } from 'ng-jhipster';
import { Response } from '@angular/http';
import { DatePipe } from '@angular/common';
import { Observable } from 'rxjs/Observable';
import { StorageService } from '../shared/storage/storage.service';
import { CrawlDataModel } from '../entities/crawl-data.model';
import { ProgressModel } from '../entities/progress.model';
import { CodeService } from '../entities/code/code.service';
import {CostFactorService} from '../entities/cost-factor/cost-factor.service';
import {CostFactor} from '../entities/cost-factor/cost-factor.model';
import { ProfitFactor } from '../entities/profit-factor/profit-factor.model';
import { ProfitFactorService } from '../entities/profit-factor/profit-factor.service';

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
    progress: ProgressModel;
    channels: Channel[];
    styles: Style[];
    types: Types[];
    isCalculate: boolean;

    // New form model
    openDate: string;
    transactions: Transactions[];
    chooseChannel: Channel;

    // Min - Max cost rate of each style
    costFactor2d: CostFactor;
    costFactor3d: CostFactor;
    costFactor4d: CostFactor;

    // Profit rate of each style
    profitFactor2d: ProfitFactor;
    profitFactor3d: ProfitFactor;
    profitFactor4d: ProfitFactor;

    private DATE_FORMAT = 'yyyy-MM-ddT12:00';
    private running: any;

    constructor(private eventManager: EventManager,
                private transactionsService: TransactionsService,
                private channelService: ChannelService,
                private styleService: StyleService,
                private typesService: TypesService,
                private alertService: AlertService,
                private datePipe: DatePipe,
                private storageService: StorageService,
                private codeService: CodeService,
                private costFactorService: CostFactorService,
                private profitFactorService: ProfitFactorService) {
    }

    ngOnInit(): void {
        this.reset();

        this.openDate = this.crawlData.openDate = this.datePipe.transform(Date.now(), this.DATE_FORMAT);
        this.progress = new ProgressModel(0, 0);
        this.onCrawlOpenDateChange();
        this.onCalculateOpenDateChange();
        this.checkCrawlProcess();
    }

    ngOnDestroy(): void {
        this.stopCheckCrawlProcess();
    }

    onCrawlOpenDateChange(): void {
        const openDate: string = this.datePipe.transform(this.crawlData.openDate, 'EEEE').toLowerCase();
        this.initCrawl(openDate);
    }

    onCalculateOpenDateChange(): void {
        const openDate = this.datePipe.transform(this.openDate, 'EEEE').toLowerCase();
        this.initCalculate(openDate);
        for (const tran of this.transactions) {
            tran.openDate = this.openDate;
        }
    }

    onChooseStyle(transactionDetails: TransactionDetails): void {
        this.costFactorService.findByStyle(transactionDetails.stylesId).subscribe(
            (costFactor: CostFactor) => {
                transactionDetails.minRate = costFactor.minRate;
                transactionDetails.maxRate = costFactor.maxRate;
            }
        );
    }

    onChangeCost2d(): void {
        for (const tran of this.transactions) {
            tran.transactionDetailsDTOs.filter((detail) => detail.stylesId === 1).forEach((detail) => {
                detail.costRate = this.costFactor2d.rate;
            });
        }
    }

    onChangeCost3d(): void {
        for (const tran of this.transactions) {
            tran.transactionDetailsDTOs.filter((detail) => detail.stylesId === 2).forEach((detail) => {
                detail.costRate = this.costFactor3d.rate;
            });
        }
    }

    onChangeCost4d(): void {
        for (const tran of this.transactions) {
            tran.transactionDetailsDTOs.filter((detail) => detail.stylesId === 3).forEach((detail) => {
                detail.costRate = this.costFactor4d.rate;
            });
        }
    }

    onChangeChannel(chooseTransaction: Transactions): void {
        for (const detail of chooseTransaction.transactionDetailsDTOs) {
            detail.channelsId = this.chooseChannel.id;
        }
    }

    addRecord(): void {

        // New model
        for (let i = 0; i < 1; i++) {
            const trans: Transactions = new Transactions();
            trans.clientsId = this.storageService.getAccountId();
            if (this.openDate) {
                trans.openDate = this.openDate;
            } else {
                trans.openDate = this.datePipe.transform(Date.now(), this.DATE_FORMAT);
            }
            trans.transactionDetailsDTOs = [];

            const detail1 = new TransactionDetails().style(1).type(1);
            const detail2 = new TransactionDetails().style(1).type(2);
            const detail3 = new TransactionDetails().style(1).type(3);
            const detail4 = new TransactionDetails().style(1).type(4);
            if (this.costFactor2d && this.costFactor2d.rate) {
                detail1.costRate = detail2.costRate = detail3.costRate = detail4.costRate = this.costFactor2d.rate;
            }
            const detail5 = new TransactionDetails().style(2).type(1);
            const detail6 = new TransactionDetails().style(2).type(2);
            const detail7 = new TransactionDetails().style(2).type(3);
            const detail8 = new TransactionDetails().style(2).type(4);
            if (this.costFactor3d && this.costFactor3d.rate) {
                detail5.costRate = detail6.costRate = detail7.costRate = detail8.costRate = this.costFactor3d.rate;
            }
            const detail9 = new TransactionDetails().style(3).type(2);
            const detail10 = new TransactionDetails().style(3).type(4);
            if (this.costFactor4d && this.costFactor4d.rate) {
                detail9.costRate = detail10.costRate = this.costFactor4d.rate;
            }
            // 2D - top
            trans.transactionDetailsDTOs.push(detail1);
            // 2D - bottom
            trans.transactionDetailsDTOs.push(detail2);
            // 2D - both
            trans.transactionDetailsDTOs.push(detail3);
            // 2D - roll
            trans.transactionDetailsDTOs.push(detail4);
            // 3D - top
            trans.transactionDetailsDTOs.push(detail5);
            // 3D - bottom
            trans.transactionDetailsDTOs.push(detail6);
            // 3D - both
            trans.transactionDetailsDTOs.push(detail7);
            // 3D - roll
            trans.transactionDetailsDTOs.push(detail8);
            // 4D - bottom
            trans.transactionDetailsDTOs.push(detail9);
            // 4D - roll
            trans.transactionDetailsDTOs.push(detail10);

            this.transactions.push(trans);
        }
    }

    // removeRecord(target: TransactionDetails): void {
    //     const idx: number = this.transaction.transactionDetailsDTOs.findIndex((t: TransactionDetails) => t === target);
    //     if (idx > -1) {
    //         this.transaction.transactionDetailsDTOs.splice(idx, 1);
    //     }
    // }

    reset(): void {
        this.crawlData = new CrawlDataModel();
        // this.transaction = new Transactions();
        // this.transaction.clientsId = this.storageService.getAccountId();
        // this.transaction.transactionDetailsDTOs = [];

        // New model
        this.transactions = [];
        this.addRecord();
    }

    check(): void {
        this.isCalculate = true;
        this.subscribeToSaveResponse(this.transactionsService.calculateList(this.transactions), 'calculate');
    }

    crawl(): void {
        this.subscribeToSaveResponse(this.codeService.crawl(this.crawlData), 'crawl');
        this.checkCrawlProcess();
    }

    trackTransactionsById(index: number, item: Transactions) {
        return item.id;
    }

    trackChannelById(index: number, item: Channel) {
        return item.id;
    }

    trackStyleById(index: number, item: Style) {
        return item.id;
    }

    trackTypesById(index: number, item: Types) {
        return item.id;
    }

    trackTransactionDetailsById(index: number, item: TransactionDetails) {
        return item.id;
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
                this.chooseChannel = this.channels[0];
                for (const tran of this.transactions) {
                    this.onChangeChannel(tran);
                }
                },
            (res: Response) => this.onError(res.json()));
        this.styleService.query().subscribe(
            (res: Response) => { this.styles = res.json(); }, (res: Response) => this.onError(res.json()));
        this.typesService.query().subscribe(
            (res: Response) => { this.types = res.json(); }, (res: Response) => this.onError(res.json()));

        // Cost factor 2d
        this.costFactorService.findByStyle(1).subscribe(
            (res: CostFactor) => {
                this.costFactor2d = res;
                this.costFactor2d.rate = this.costFactor2d.minRate;
                this.onChangeCost2d();
            },
            (res: Response) => this.onError(res.json())
        );

        // Cost factor 3d
        this.costFactorService.findByStyle(2).subscribe(
            (res: CostFactor) => {
                this.costFactor3d = res;
                this.costFactor3d.rate = this.costFactor3d.minRate;
                this.onChangeCost3d();
            },
            (res: Response) => this.onError(res.json())
        );

        // Cost factor 4d
        this.costFactorService.findByStyle(3).subscribe(
            (res: CostFactor) => {
                this.costFactor4d = res;
                this.costFactor4d.rate = this.costFactor4d.minRate;
                this.onChangeCost4d();
            },
            (res: Response) => this.onError(res.json())
        );

        // Profit factor 2d
        this.profitFactorService.findByStyle(1).subscribe(
            (res: ProfitFactor) => {
                this.profitFactor2d = res;
            },
            (res: Response) => this.onError(res.json())
        );

        // Profit factor 3d
        this.profitFactorService.findByStyle(2).subscribe(
            (res: ProfitFactor) => {
                this.profitFactor3d = res;
            },
            (res: Response) => this.onError(res.json())
        );

        // Profit factor 4d
        this.profitFactorService.findByStyle(2).subscribe(
            (res: ProfitFactor) => {
                this.profitFactor4d = res;
            },
            (res: Response) => this.onError(res.json())
        );
    }

    private checkCrawlProcess(): void {
        this.running = setInterval(() => {
            this.getCrawlProcess();
        }, 100);
    }

    private stopCheckCrawlProcess(): void {
        clearInterval(this.running);
    }

    private getCrawlProcess(): void {
        this.codeService.crawlProcessing().subscribe((res: Response) => {
            this.progress = res.json();
                if (this.progress.total === 0) {
                    this.stopCheckCrawlProcess();
                }
            },
            (res: Response) => this.onError(res.json())
        );
    }

    private subscribeToSaveResponse(result: Observable<any>, instance: string) {
        result.subscribe((res: any) =>
            this.onSaveSuccess(res, instance), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: any, instance: string) {
        if (instance === 'calculate') {
            this.eventManager.broadcast({ name: 'channelListModification', content: 'OK'});
            this.transactions = result;
            for (const tran of this.transactions) {
                tran.openDate = this.datePipe.transform(tran.openDate, this.DATE_FORMAT);
            }
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
