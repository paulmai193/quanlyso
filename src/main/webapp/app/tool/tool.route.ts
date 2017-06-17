import { Routes } from '@angular/router';
import { QuanLySoToolComponent } from './tool.component';
import { UserRouteAccessService } from '../shared/auth/user-route-access-service';
/**
 * Created by Dai Mai on 6/17/17.
 */
export const toolRoute: Routes = [
    {
        path: 'tool',
        component: QuanLySoToolComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN'],
            pageTitle: 'global.menu.tool'
        },
        canActivate: [UserRouteAccessService]
    }
];
