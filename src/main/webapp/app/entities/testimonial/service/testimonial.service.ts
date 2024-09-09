import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestimonial, NewTestimonial } from '../testimonial.model';

export type PartialUpdateTestimonial = Partial<ITestimonial> & Pick<ITestimonial, 'id'>;

type RestOf<T extends ITestimonial | NewTestimonial> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestTestimonial = RestOf<ITestimonial>;

export type NewRestTestimonial = RestOf<NewTestimonial>;

export type PartialUpdateRestTestimonial = RestOf<PartialUpdateTestimonial>;

export type EntityResponseType = HttpResponse<ITestimonial>;
export type EntityArrayResponseType = HttpResponse<ITestimonial[]>;

@Injectable({ providedIn: 'root' })
export class TestimonialService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/testimonials');

  create(testimonial: NewTestimonial): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(testimonial);
    return this.http
      .post<RestTestimonial>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(testimonial: ITestimonial): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(testimonial);
    return this.http
      .put<RestTestimonial>(`${this.resourceUrl}/${this.getTestimonialIdentifier(testimonial)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(testimonial: PartialUpdateTestimonial): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(testimonial);
    return this.http
      .patch<RestTestimonial>(`${this.resourceUrl}/${this.getTestimonialIdentifier(testimonial)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTestimonial>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTestimonial[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestimonialIdentifier(testimonial: Pick<ITestimonial, 'id'>): number {
    return testimonial.id;
  }

  compareTestimonial(o1: Pick<ITestimonial, 'id'> | null, o2: Pick<ITestimonial, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestimonialIdentifier(o1) === this.getTestimonialIdentifier(o2) : o1 === o2;
  }

  addTestimonialToCollectionIfMissing<Type extends Pick<ITestimonial, 'id'>>(
    testimonialCollection: Type[],
    ...testimonialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testimonials: Type[] = testimonialsToCheck.filter(isPresent);
    if (testimonials.length > 0) {
      const testimonialCollectionIdentifiers = testimonialCollection.map(testimonialItem => this.getTestimonialIdentifier(testimonialItem));
      const testimonialsToAdd = testimonials.filter(testimonialItem => {
        const testimonialIdentifier = this.getTestimonialIdentifier(testimonialItem);
        if (testimonialCollectionIdentifiers.includes(testimonialIdentifier)) {
          return false;
        }
        testimonialCollectionIdentifiers.push(testimonialIdentifier);
        return true;
      });
      return [...testimonialsToAdd, ...testimonialCollection];
    }
    return testimonialCollection;
  }

  protected convertDateFromClient<T extends ITestimonial | NewTestimonial | PartialUpdateTestimonial>(testimonial: T): RestOf<T> {
    return {
      ...testimonial,
      date: testimonial.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTestimonial: RestTestimonial): ITestimonial {
    return {
      ...restTestimonial,
      date: restTestimonial.date ? dayjs(restTestimonial.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTestimonial>): HttpResponse<ITestimonial> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTestimonial[]>): HttpResponse<ITestimonial[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
