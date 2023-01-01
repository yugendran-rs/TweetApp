export const capitalize = (str1, str2) =>(
  str1
    .charAt(0)
    .toUpperCase()
    .concat(str1.slice(1))
    .concat(" ")
    .concat(str2.charAt(0).toUpperCase().concat(str2.slice(1))));