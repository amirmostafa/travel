import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TestimonialComponent } from './list/testimonial.component';
import { TestimonialDetailComponent } from './detail/testimonial-detail.component';
import { TestimonialUpdateComponent } from './update/testimonial-update.component';
import TestimonialResolve from './route/testimonial-routing-resolve.service';

const testimonialRoute: Routes = [
  {
    path: '',
    component: TestimonialComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TestimonialDetailComponent,
    resolve: {
      testimonial: TestimonialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TestimonialUpdateComponent,
    resolve: {
      testimonial: TestimonialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TestimonialUpdateComponent,
    resolve: {
      testimonial: TestimonialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testimonialRoute;
