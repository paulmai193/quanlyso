import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { QuanlysoStyleModule } from './style/style.module';
import { QuanlysoTypesModule } from './types/types.module';
import { QuanlysoChannelModule } from './channel/channel.module';
import { QuanlysoProfitFactorModule } from './profit-factor/profit-factor.module';
import { QuanlysoCostFactorModule } from './cost-factor/cost-factor.module';
import { QuanlysoTransactionsModule } from './transactions/transactions.module';
import { QuanlysoTransactionDetailsModule } from './transaction-details/transaction-details.module';
import { QuanlysoClientModule } from './client/client.module';
import { QuanlysoCodeModule } from './code/code.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        QuanlysoStyleModule,
        QuanlysoTypesModule,
        QuanlysoChannelModule,
        QuanlysoProfitFactorModule,
        QuanlysoCostFactorModule,
        QuanlysoTransactionsModule,
        QuanlysoTransactionDetailsModule,
        QuanlysoClientModule,
        QuanlysoCodeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoEntityModule {}
