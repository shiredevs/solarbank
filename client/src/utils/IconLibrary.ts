import { library } from '@fortawesome/fontawesome-svg-core';
import { faCloudSun, faXmark, IconDefinition } from '@fortawesome/free-solid-svg-icons';

export type ApplicationIcons = {
  [key: string]: IconDefinition;
};

const applicationIcons: ApplicationIcons = {
  LANDING_PAGE: faCloudSun,
  FALLBACK: faXmark
};

Object.values(applicationIcons).forEach(icon => library.add(icon));

export default applicationIcons;
