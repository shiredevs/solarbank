import { library } from '@fortawesome/fontawesome-svg-core';
import { faCloudSun, faXmark, IconDefinition } from '@fortawesome/free-solid-svg-icons';

const applicationIcons: IconDefinition[] = [faXmark, faCloudSun];

applicationIcons.forEach(icon => library.add(icon));

export default applicationIcons;
