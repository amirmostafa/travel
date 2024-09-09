import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgencyService, NewAgencyService } from '../agency-service.model';

export type PartialUpdateAgencyService = Partial<IAgencyService> & Pick<IAgencyService, 'id'>;

export type EntityResponseType = HttpResponse<IAgencyService>;
export type EntityArrayResponseType = HttpResponse<IAgencyService[]>;

@Injectable({ providedIn: 'root' })
export class AgencyServiceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agency-services');

  create(agencyService: NewAgencyService): Observable<EntityResponseType> {
    return this.http.post<IAgencyService>(this.resourceUrl, agencyService, { observe: 'response' });
  }

  update(agencyService: IAgencyService): Observable<EntityResponseType> {
    return this.http.put<IAgencyService>(`${this.resourceUrl}/${this.getAgencyServiceIdentifier(agencyService)}`, agencyService, {
      observe: 'response',
    });
  }

  partialUpdate(agencyService: PartialUpdateAgencyService): Observable<EntityResponseType> {
    return this.http.patch<IAgencyService>(`${this.resourceUrl}/${this.getAgencyServiceIdentifier(agencyService)}`, agencyService, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgencyService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgencyService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgencyServiceIdentifier(agencyService: Pick<IAgencyService, 'id'>): number {
    return agencyService.id;
  }

  compareAgencyService(o1: Pick<IAgencyService, 'id'> | null, o2: Pick<IAgencyService, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgencyServiceIdentifier(o1) === this.getAgencyServiceIdentifier(o2) : o1 === o2;
  }

  addAgencyServiceToCollectionIfMissing<Type extends Pick<IAgencyService, 'id'>>(
    agencyServiceCollection: Type[],
    ...agencyServicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agencyServices: Type[] = agencyServicesToCheck.filter(isPresent);
    if (agencyServices.length > 0) {
      const agencyServiceCollectionIdentifiers = agencyServiceCollection.map(agencyServiceItem =>
        this.getAgencyServiceIdentifier(agencyServiceItem),
      );
      const agencyServicesToAdd = agencyServices.filter(agencyServiceItem => {
        const agencyServiceIdentifier = this.getAgencyServiceIdentifier(agencyServiceItem);
        if (agencyServiceCollectionIdentifiers.includes(agencyServiceIdentifier)) {
          return false;
        }
        agencyServiceCollectionIdentifiers.push(agencyServiceIdentifier);
        return true;
      });
      return [...agencyServicesToAdd, ...agencyServiceCollection];
    }
    return agencyServiceCollection;
  }
}
