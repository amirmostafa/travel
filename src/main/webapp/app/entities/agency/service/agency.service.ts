import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgency, NewAgency } from '../agency.model';

export type PartialUpdateAgency = Partial<IAgency> & Pick<IAgency, 'id'>;

export type EntityResponseType = HttpResponse<IAgency>;
export type EntityArrayResponseType = HttpResponse<IAgency[]>;

@Injectable({ providedIn: 'root' })
export class AgencyService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agencies');

  create(agency: NewAgency): Observable<EntityResponseType> {
    return this.http.post<IAgency>(this.resourceUrl, agency, { observe: 'response' });
  }

  update(agency: IAgency): Observable<EntityResponseType> {
    return this.http.put<IAgency>(`${this.resourceUrl}/${this.getAgencyIdentifier(agency)}`, agency, { observe: 'response' });
  }

  partialUpdate(agency: PartialUpdateAgency): Observable<EntityResponseType> {
    return this.http.patch<IAgency>(`${this.resourceUrl}/${this.getAgencyIdentifier(agency)}`, agency, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgencyIdentifier(agency: Pick<IAgency, 'id'>): number {
    return agency.id;
  }

  compareAgency(o1: Pick<IAgency, 'id'> | null, o2: Pick<IAgency, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgencyIdentifier(o1) === this.getAgencyIdentifier(o2) : o1 === o2;
  }

  addAgencyToCollectionIfMissing<Type extends Pick<IAgency, 'id'>>(
    agencyCollection: Type[],
    ...agenciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agencies: Type[] = agenciesToCheck.filter(isPresent);
    if (agencies.length > 0) {
      const agencyCollectionIdentifiers = agencyCollection.map(agencyItem => this.getAgencyIdentifier(agencyItem));
      const agenciesToAdd = agencies.filter(agencyItem => {
        const agencyIdentifier = this.getAgencyIdentifier(agencyItem);
        if (agencyCollectionIdentifiers.includes(agencyIdentifier)) {
          return false;
        }
        agencyCollectionIdentifiers.push(agencyIdentifier);
        return true;
      });
      return [...agenciesToAdd, ...agencyCollection];
    }
    return agencyCollection;
  }
}
