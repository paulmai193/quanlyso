/**
 * Created by Dai Mai on 6/17/17.
 */

import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { QuanlysoSharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { QuanLySoToolComponent } from './tool.component';
import { TransactionsService } from '../entities/transactions/transactions.service';
import { toolRoute } from './tool.route';
import { ChannelService } from '../entities/channel/channel.service';
import { StyleService } from '../entities/style/style.service';
import { TypesService } from '../entities/types/types.service';
import { StorageService } from '../shared/storage/storage.service';
import { CodeService } from '../entities/code/code.service';
import { CostFactorService } from '../entities/cost-factor/cost-factor.service';
import { ProfitFactorService } from '../entities/profit-factor/profit-factor.service';
const ENTITY_STATES = [
    ...toolRoute
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        QuanLySoToolComponent,
    ],
    providers: [
        TransactionsService,
        ChannelService,
        StyleService,
        TypesService,
        StorageService,
        CodeService,
        CostFactorService,
        ProfitFactorService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanLySoToolModule {}
