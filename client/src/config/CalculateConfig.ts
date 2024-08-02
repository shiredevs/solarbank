
export type CalculateConfig = {
 [key: string]: string | undefined
}

const config: CalculateConfig = {
  SERVER_URL: process.env.SERVER_URL,
  CALCULATE_ENDPOINT: process.env.CALCULATE_ENDPOINT
}

export { config }
