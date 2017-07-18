/**
 * Created by Dai Mai on 6/17/17.
 */

import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { QuanlysoSharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { QuanLySoToolComponent } from './tool.component';
import { TransactionsService } from '../entities/transactions/transactions.service';
import { toolRoute } from './tool.route';
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
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanLySoToolModule {}
