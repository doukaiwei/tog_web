import http from '@/utils/request/requestAlova';
export const queryVlogsList = (param: Record<string, any>) => http.post("togs/getVlogs/QueryList", param)
export const deleteVlog = (param: Record<string, any>) => http.get("togs/getVlogs/deleteVlogs", param)
